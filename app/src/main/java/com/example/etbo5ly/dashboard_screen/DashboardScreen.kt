package com.example.etbo5ly.dashboard_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.etbo5ly.dashboard_screen.components.DashboardAppBarComponent
import com.example.etbo5ly.dashboard_screen.components.MealOfDayCard
import com.example.etbo5ly.dashboard_screen.components.RecipeCard
import com.example.etbo5ly.ui.theme.Etbo5lyTheme

@Composable
fun DashboardScreen(userName: String = "Guest") {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DashboardAppBarComponent(userName)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Meal of the Day
            item {
                MealOfDayCard({})
                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    Text(
                        text = "Categories",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        for (n in 1..5) {
                            Text(
                                text = "$n",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Trending Now",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Recipes
            items(5) { index ->
                RecipeCard({},true)
                // Add spacing between cards except after the last one
                if (index < 4) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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