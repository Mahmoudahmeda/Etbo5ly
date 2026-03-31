package com.example.etbo5ly.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.etbo5ly.data.remote.RetrofitInstance
import com.example.etbo5ly.ui.categories.CategoriesSection
import com.example.etbo5ly.ui.categories.Category

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var selectedItem by remember { mutableStateOf("Home") }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getCategories()
            categories = response.categories.map {
                Category(
                    name = it.strCategory,
                    image = it.strCategoryThumb
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CategoriesSection(categories)
        Spacer(modifier = Modifier.weight(1f))
        BottomNavBar(
            selectedItem = selectedItem,
            onItemClick = { selectedItem = it }
        )
    }
}