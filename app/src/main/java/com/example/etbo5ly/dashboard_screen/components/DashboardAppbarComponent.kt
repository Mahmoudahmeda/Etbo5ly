package com.example.etbo5ly.dashboard_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.AppBarColorShade

@Composable
fun DashboardAppBarComponent(
    name: String,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onFavouriteClick: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(AppBarColor, AppBarColorShade)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(gradient)
            .padding(horizontal = 20.dp, vertical = 34.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menu Icon — Left
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Greeting
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Hello $name 👋",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready to cook something delicious today?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Favourite Icon — Right
            IconButton(onClick = onFavouriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favourites",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}