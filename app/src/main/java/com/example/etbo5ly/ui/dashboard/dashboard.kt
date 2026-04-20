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
import androidx.navigation.NavController
import com.example.etbo5ly.ui.categories.CategoriesSection
import com.example.etbo5ly.ui.categories.Category
import com.example.etbo5ly.dashboard_screen.DashboardScreen
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import com.google.android.gms.common.api.Api

@Composable
fun DashboardScreen(modifier: Modifier = Modifier, navcontroller: NavController) {
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var selectedItem by remember { mutableStateOf("Home") }
    val apiclient = ApiClient.service
    LaunchedEffect(Unit) {
        try {
            val response = apiclient.getCategories()
            categories = response.body()?.categories?.map {
                Category(
                    name = it.strCategory,
                    image = it.strCategoryThumb
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        DashboardScreen(
            modifier = Modifier,
            userName = "Guest",
            navcontroller = navcontroller
        )
        CategoriesSection(categories)
        Spacer(modifier = Modifier.weight(1f))
        BottomNavBar(
            selectedItem = selectedItem,
            onItemClick = { selectedItem = it },
            navController = navcontroller
        )
    }
}