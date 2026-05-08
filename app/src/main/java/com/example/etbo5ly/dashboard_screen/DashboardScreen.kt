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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.dashboard_screen.components.DashboardAppBarComponent
import com.example.etbo5ly.dashboard_screen.components.DrawerContent
import com.example.etbo5ly.dashboard_screen.components.RecipeCard
import com.example.etbo5ly.data.local.Etbo5lyDataBase
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import com.example.etbo5ly.data.repository.CalendarRepo
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.data.repository.MealRepository
import com.example.etbo5ly.ui.categories.CategoriesSection
import com.example.etbo5ly.ui.components.NoInternetScreen
import com.example.etbo5ly.ui.dashboard.BottomNavBar
import com.example.etbo5ly.utils.isInternetAvailable
import com.example.etbo5ly.utils.observeNetworkConnectivity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    isGuest: Boolean = false
) {
    val context = LocalContext.current

    val isOnline by observeNetworkConnectivity(context)
        .collectAsState(initial = isInternetAvailable(context))

    val apiService = ApiClient.service
    val remoteDataSource = RemoteDataSource(apiService)
    val repository = MealRepository(remoteDataSource)
    val authRepo = AuthenticationRepo()
    val database = remember { Etbo5lyDataBase.getDataBase(context) }
    val calendarRepo = CalendarRepository(database.mealDao())
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(repository, calendarRepo)
    )

    val meal by viewModel.meal.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val favouriteIds by viewModel.favouriteIds.collectAsState()
    val isLoggedOut by viewModel.isLoggedOut.collectAsState()
    val showGuestFavouriteDialog by viewModel.showGuestFavouriteDialog.collectAsState()

    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Navbar state
    var selectedNavItem by remember { mutableStateOf("Home") }
    var userName by remember { mutableStateOf("Guest") }

    if (!isGuest) {
        authRepo.getCurrentUserName().also { userName = it }
    }

    // Auto retry when internet comes back
    LaunchedEffect(isOnline) {
        if (isOnline && (meal == null || recipes.isEmpty())) {
            viewModel.retry()
        }
    }

    // Handle logout navigation
    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            navController.navigate("login") {
                popUpTo("home?isGuest=false") { inclusive = true }
            }
        }
    }

    // Guest favourite dialog
    if (showGuestFavouriteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissGuestFavouriteDialog() },
            containerColor = Color(0xFF1E2228),
            title = {
                Text(
                    text = "Login Required",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "You need to login or signup to add recipes to your favourites.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissGuestFavouriteDialog()
                        navController.navigate("login") {
                            popUpTo("home?isGuest=true") { inclusive = true }
                        }
                    }
                ) {
                    Text(
                        text = "Login",
                        color = Color.Cyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissGuestFavouriteDialog() }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userName = userName,
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    if (isGuest) {
                        navController.navigate("login") {
                            popUpTo("home?isGuest=true") { inclusive = true }
                        }
                    } else {
                        navController.navigate("Profile")
                    }
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    if (isGuest) {
                        navController.navigate("login") {
                            popUpTo("home?isGuest=true") { inclusive = true }
                        }
                    } else {
                        viewModel.logout()
                    }
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
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onFavouriteClick = {
                        if (isGuest) {
                            viewModel.showGuestDialog()
                        } else {
                            navController.navigate("Favourite")
                        }
                    }
                )
            },
            bottomBar = {
                // Bottom Navigation Bar
                BottomNavBar(
                    selectedItem = selectedNavItem,
                    onItemClick = { selectedNavItem = it },
                    navController = navController
                )
            }
        ) { innerPadding ->

            // Show no internet animation when offline
            if (!isOnline) {
                NoInternetScreen()
                return@Scaffold
            }

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

                // Only show error when online
                error != null && isOnline -> {
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
                                RecipeCard(
                                    onFavClick = {
                                        viewModel.onFavoriteClick(currentMeal)
                                        val message = if (favouriteIds.contains(currentMeal.idMeal))
                                            "Removed from favourites"
                                        else
                                            "Added to favourites"
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    },
                                    isFavorite = favouriteIds.contains(currentMeal.idMeal),
                                    modifier = Modifier,
                                    meal = currentMeal,
                                    navController= navController
                                )
                            }
                        }

                        item {
                            // Categories Section
                            Spacer(modifier = Modifier.height(12.dp))
                            CategoriesSection(
                                categories,
                                navController = navController
                            )
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
                                meal = recipe,
                                navController = navController
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