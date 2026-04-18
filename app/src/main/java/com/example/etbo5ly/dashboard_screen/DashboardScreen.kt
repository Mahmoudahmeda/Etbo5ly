package com.example.etbo5ly.dashboard_screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.etbo5ly.dashboard_screen.components.DashboardAppBarComponent
import com.example.etbo5ly.dashboard_screen.components.DrawerContent
import com.example.etbo5ly.dashboard_screen.components.MealOfDayCard
import com.example.etbo5ly.dashboard_screen.components.RecipeCard
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import com.example.etbo5ly.data.repository.MealRepository
import com.example.etbo5ly.ui.categories.CategoriesSection
import com.example.etbo5ly.ui.dashboard.BottomNavBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    userName: String = "Guest",
    navController: NavController
) {
    val context = LocalContext.current

    // Networking Setup
    val apiService = ApiClient.service
    val remoteDataSource = RemoteDataSource(apiService)
    val repository = MealRepository(remoteDataSource)

    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(repository, context)
    )

    val meal by viewModel.meal.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val favouriteIds by viewModel.favouriteIds.collectAsState()
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()

    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Navbar state
    var selectedNavItem by remember { mutableStateOf("Home") }

    // Handle logout navigation
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userName = userName,
                onProfileClick = {
                    scope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    viewModel.logout()
                }
            )
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                // Top App Bar
                DashboardAppBarComponent(
                    name = userName,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onFavouriteClick = {
                        // will wire up when favourites screen is ready
                    }
                )
            },
            bottomBar = {
                // Bottom Navigation Bar
                BottomNavBar(
                    selectedItem = selectedNavItem,
                    onItemClick = { selectedNavItem = it }
                )
            }
        ) { innerPadding ->

            when {

                isLoading && meal == null && recipes.isEmpty() -> {
                    // Loading Indicator
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    // Error Message
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        item {
                            // Meal of the Day Card
                            meal?.let { currentMeal ->
                                MealOfDayCard(
                                    onClick = { },
                                    meal = currentMeal,
                                    modifier = Modifier
                                )
                            }
                        }

                        item {
                            // Categories Section
                            Spacer(modifier = Modifier.height(12.dp))
                            CategoriesSection(categories)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            // Recipes Header
                            Text(
                                text = "Recipes",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(recipes) { recipe ->
                            // Recipe Card
                            RecipeCard(
                                onFavClick = {
                                    viewModel.onFavoriteClick(recipe)
                                    val message = if (favouriteIds.contains(recipe.idMeal))
                                        "Removed from favourites"
                                    else
                                        "Added to favourites"
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                },
                                isFavorite = favouriteIds.contains(recipe.idMeal),
                                modifier = Modifier,
                                meal = recipe
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}