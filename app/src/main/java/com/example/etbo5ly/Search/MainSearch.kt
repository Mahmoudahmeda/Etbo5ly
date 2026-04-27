package com.example.etbo5ly.Search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Egg
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.etbo5ly.Search.components.CategoryCard
import com.example.etbo5ly.Search.components.CountryCard
import com.example.etbo5ly.Search.components.FilterButton
import com.example.etbo5ly.Search.components.IngredientCard
import com.example.etbo5ly.Search.components.ResponseMealCard
import com.example.etbo5ly.Search.components.SearchGrid
import com.example.etbo5ly.ui.components.Etbo5lyAppBar
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.Etbo5lyTheme
import kotlinx.coroutines.delay

data class FilterOption(val name: String, val icon: ImageVector)

@Composable
fun MainSearch(
    navController: NavController,
    viewModel: Search = viewModel()
) {
    val searchQ by viewModel.searchQ.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val categories by viewModel.categories.collectAsState()
    val areas by viewModel.areas.collectAsState()
    val ingredients by viewModel.ingredients.collectAsState()
    val general by viewModel.general.collectAsState()

    var selectedFilter by remember { mutableStateOf("General") }
    val filters = listOf(
        FilterOption("Categories", Icons.Default.Restaurant),
        FilterOption("Countries", Icons.Default.Public),
        FilterOption("Ingredients", Icons.Outlined.Egg)
    )

    LaunchedEffect(selectedFilter,searchQ) {
        when (selectedFilter) {
            "Categories" -> viewModel.fetchCategories()
            "Countries" -> viewModel.fetchAreas()
            "Ingredients" -> viewModel.fetchIngredients()
            "General" -> {
                if (searchQ.isNotBlank()) {
                    delay(500)
                    viewModel.searchGeneral(searchQ)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Etbo5lyAppBar(navController = navController, text = "Search")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            // 1. Custom Search Bar
            TextField(
                value = searchQ,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = { Text("What are you Looking For ?", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1C1C1E),
                    unfocusedContainerColor = Color(0xFF1C1C1E),
                    disabledContainerColor = Color(0xFF1C1C1E),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true
            )

            // 2. Filter Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter.name
                    FilterButton(
                        option = filter,
                        isSelected = isSelected,
                        onClick = { 
                            selectedFilter = if (isSelected) "General" else filter.name
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 3. Conditional Content: Loading, Error, or Grid
            Box(modifier = Modifier.weight(1f)) {
                if (error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = error ?: "An error occurred",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    // Dynamic Grid based on Selection with Local Filtering
                    when (selectedFilter) {
                        "Categories" -> {
                            val filteredList = categories.filter { it.strCategory.contains(searchQ, ignoreCase = true) }
                            if (filteredList.isEmpty() && searchQ.isNotEmpty()) {
                                EmptyState(searchQ)
                            } else {
                                SearchGrid(items = filteredList, columns = 2) { category ->
                                    CategoryCard(category = category, onClick = {
                                        navController.navigate("searchResult/category/${category.strCategory}")
                                    })
                                }
                            }
                        }
                        "Countries" -> {
                            val filteredList = areas.filter { it.strArea.contains(searchQ, ignoreCase = true) }
                            if (filteredList.isEmpty() && searchQ.isNotEmpty()) {
                                EmptyState(searchQ)
                            } else {
                                SearchGrid(items = filteredList, columns = 3) { area ->
                                    CountryCard(area = area, onClick = {
                                        navController.navigate("searchResult/country/${area.strArea}")
                                    })
                                }
                            }
                        }
                        "Ingredients" -> {
                            val filteredList = ingredients.filter { it.strIngredient.contains(searchQ, ignoreCase = true) }
                            if (filteredList.isEmpty() && searchQ.isNotEmpty()) {
                                EmptyState(searchQ)
                            } else {
                                SearchGrid(items = filteredList, columns = 3) { ingredient ->
                                    IngredientCard(ingredient = ingredient, onClick = {
                                        navController.navigate("searchResult/ingredient/${ingredient.strIngredient}")
                                    })
                                }
                            }
                        }
                        "General" -> {
                            if (isLoading) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = AppBarColor)
                                }
                            } else if (general.isEmpty() && searchQ.isNotEmpty()) {
                                EmptyState(searchQ)
                            } else {
                                SearchGrid(general, columns = 2) { meal ->
                                    ResponseMealCard(meal, navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(query: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No matches found for '$query'",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainSearchPreview() {
    Etbo5lyTheme {
        MainSearch(navController = rememberNavController())
    }
}
