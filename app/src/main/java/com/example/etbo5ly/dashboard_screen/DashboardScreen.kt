package com.example.etbo5ly.dashboard_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.etbo5ly.dashboard_screen.components.DashboardAppBarComponent
import com.example.etbo5ly.dashboard_screen.components.MealOfDayCard
import com.example.etbo5ly.dashboard_screen.components.RecipeCard
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import com.example.etbo5ly.data.repository.MealRepository
import com.example.etbo5ly.ui.theme.Etbo5lyTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.etbo5ly.ui.categories.CategoriesSection
import com.example.etbo5ly.ui.dashboard.BottomNavBar
import com.example.etbo5ly.ui.dashboard.DashboardScreen

@Composable
fun DashboardScreen(
    userName: String = "Guest",
    navcontroller: NavController
) {
    val apiService = ApiClient.service
    val remoteDataSource = RemoteDataSource(apiService)
    val repository = MealRepository(remoteDataSource)

    // Create ViewModel with the repository
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(repository)
    )

    val meal by viewModel.meal.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val category by viewModel.category.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DashboardAppBarComponent(userName)
        }
    ) { innerPadding ->

        when {
            isLoading && meal == null && recipes.isEmpty() -> {
                // Show loading only on first load
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
                // Show error message
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
                    // Meal of the Day
                    item {
                        meal?.let { currentMeal ->
                            MealOfDayCard(
                                onClick = { /* Navigate to recipe detail */ },
                                meal = currentMeal,
                                modifier = Modifier
                            )
                        }
                    }

                    // Categories Section
                    item {
                        CategoriesSection(category)
                    }

                    // Trending Now Header
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Recipes",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Recipes List
                    items(recipes) { recipe ->
                        RecipeCard(
                            onFavClick = { viewModel.onFavoriteClick(recipe.idMeal) },
                            isFavorite = false,
                            modifier = Modifier,
                            meal = recipe
                        )
                    }

                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            BottomNavBar(
                selectedItem = selectedItem,
                onItemClick = {
                    viewModel.selectItem(it)
                    navcontroller.navigate(it)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    Etbo5lyTheme {
        DashboardScreen()
    }
}