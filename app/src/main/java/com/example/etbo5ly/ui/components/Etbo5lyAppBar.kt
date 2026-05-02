package com.example.etbo5ly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.AppBarColorShade

// AppBar for the App screens takes
// 1. modifier (optional)
// 2. navController (required)
// 3. text : screen name (required)
// why use?
// has the gradiant color same as Figma design
// used in most of the screens of the app

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Etbo5lyAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    text: String
){
    val gradient = Brush.verticalGradient(
        colors = listOf(AppBarColor, AppBarColorShade)
    )

    Box(modifier = Modifier.background(gradient)) {
        TopAppBar(
            title = { Text(text = text, color = Color.Black) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.Black,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )
    }
}