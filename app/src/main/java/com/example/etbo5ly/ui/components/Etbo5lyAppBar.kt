package com.example.etbo5ly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.etbo5ly.settings.SettingsViewModel
import com.example.etbo5ly.ui.theme.AppBarColor
import com.example.etbo5ly.ui.theme.AppBarColorShade
import com.example.etbo5ly.ui.theme.CardBottom

// AppBar for the App screens takes
// 1. modifier (optional)
// 2. navController (required)
// 3. text : screen name (required)
// why use?
// has the gradiant color same as Figma design
// used in most of the screens of the app
// has the user photo in the top right corner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Etbo5lyAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    text: String
){
    val viewModel: SettingsViewModel = viewModel()
    val userPhotoUrl by viewModel.userPhotoUrl.collectAsState()

    val gradient = Brush.verticalGradient(
        colors = listOf(AppBarColor, AppBarColorShade)
    )

    Box(modifier = modifier.background(gradient)) {
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
            actions = {
                AsyncImage(
                    model = userPhotoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CardBottom)
                        .clickable { 
                            // Only navigate if we aren't already on the profile screen
                            if (navController.currentDestination?.route != "Profile") {
                                navController.navigate("Profile")
                            }
                        },
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(Icons.Default.Person),
                    fallback = rememberVectorPainter(Icons.Default.Person),
                )
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
