package com.example.etbo5ly.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.etbo5ly.data.dto.Meal

@Composable
fun FavouritesScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: FavouritesViewModel = viewModel(
        factory = FavouritesViewModelFactory(context)
    )

    val favourites by viewModel.favourites.collectAsState()

    // meal selected for removal confirmation dialog
    var mealToRemove by remember { mutableStateOf<Meal?>(null) }

    // Confirmation Dialog
    mealToRemove?.let { meal ->
        AlertDialog(
            onDismissRequest = { mealToRemove = null },
            containerColor = Color(0xFF1E2228),
            title = {
                Text(
                    text = "Remove Favourite?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${meal.strMeal}\" from your favourites?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeFavourite(meal)
                        mealToRemove = null
                    }
                ) {
                    Text(
                        text = "Remove",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { mealToRemove = null }) {
                    Text(
                        text = "Cancel",
                        color = Color.Cyan
                    )
                }
            }
        )
    }

    // Main Screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13171F))
    ) {

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A9E9E))
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Favourites",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Content
        if (favourites.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No favourites yet",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Tap the heart icon on any recipe to save it",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            // Recipes count
            Text(
                text = "${favourites.size} Recipe${if (favourites.size > 1) "s" else ""} saved",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Favourites List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favourites) { meal ->
                    FavouriteCard(
                        meal = meal,
                        onRemoveClick = { mealToRemove = meal },
                        onCardClick = {
                            navController.navigate("details/${meal.idMeal}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavouriteCard(
    meal: Meal,
    onRemoveClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E2228)
        )
    ) {
        Column {

            // Meal Image with Category tag
            Box {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = meal.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // Category tag — top right
                Text(
                    text = meal.strCategory,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(
                            color = Color(0xFF1A9E9E),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // Meal info row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Meal name
                Text(
                    text = meal.strMeal,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Remove favourite button
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Remove from favourites",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}