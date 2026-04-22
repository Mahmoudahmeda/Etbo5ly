package com.example.etbo5ly.Search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.etbo5ly.Search.components.ResponseMealCard
import com.example.etbo5ly.Search.components.SearchGrid
import com.example.etbo5ly.ui.components.Etbo5lyAppBar

@Composable
fun SearchResult(
    navController: NavController,
    filterType: String?,
    selectedItem: String?,
    viewModel: Search = Search()
) {
    val result by viewModel.result.collectAsState()
    val meals = result?.meals ?: emptyList()

    LaunchedEffect(filterType, selectedItem) {
        if (filterType != null && selectedItem != null) {
            viewModel.search(filterType, selectedItem)
        }
    }

    Scaffold(
        topBar = {
            Etbo5lyAppBar(
                navController = navController, 
                text = selectedItem ?: "$selectedItem"
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchGrid(
                items = meals,
                columns = 2,
                modifier = Modifier.weight(1f)
            ) { meal ->
                ResponseMealCard(
                    meal = meal, 
                    navcontroller = navController
                )
            }
        }
    }
}
