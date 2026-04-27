package com.example.etbo5ly.dashboard_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.etbo5ly.dashboard_screen.components.DashboardAppBarComponent
import com.example.etbo5ly.dashboard_screen.components.MealOfDayCard
import com.example.etbo5ly.dashboard_screen.components.RecipeCard
import com.example.etbo5ly.data.dto.CategoryDto
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import com.example.etbo5ly.data.repository.MealRepository
import com.example.etbo5ly.ui.categories.CategoriesSection
import com.example.etbo5ly.ui.categories.Category
import com.example.etbo5ly.ui.dashboard.BottomNavBar

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    userName: String = "Guest",
    navcontroller: NavController
) {
    // Networking Setup
    val apiService = ApiClient.service
    val remoteDataSource = RemoteDataSource(apiService)
    val repository = MealRepository(remoteDataSource)

    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(repository)
    )

    val meal by viewModel.meal.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // State
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var selectedNavItem by remember { mutableStateOf("Home") }

    // Load Categories
    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.service.getCategories()
            if (response.isSuccessful) {
                categories = response.body()?.categories?.map { categoryDto: CategoryDto ->
                    Category(
                        name = categoryDto.strCategory,
                        image = categoryDto.strCategoryThumb
                    )
                } ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // Top App Bar
            DashboardAppBarComponent(userName)
        },
        bottomBar = {
            // Bottom Navigation Bar
            BottomNavBar(
                selectedItem = selectedNavItem,
                onItemClick = { selectedNavItem = it },
                navController = navcontroller
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
                                onClick = { navcontroller.navigate("details/${meal?.idMeal}")},
                                meal = currentMeal,
                                modifier = Modifier
                            )
                        }
                    }

                    item {
                        // Categories Section
                        Spacer(modifier = Modifier.height(12.dp))
                        CategoriesSection(categories, navcontroller)
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
                                viewModel.onFavoriteClick(recipe.idMeal)
                            },
                            isFavorite = false,
                            modifier = Modifier,
                            meal = recipe,
                            navController = navcontroller
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