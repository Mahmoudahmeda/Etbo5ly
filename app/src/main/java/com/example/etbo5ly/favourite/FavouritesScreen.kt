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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.etbo5ly.R
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.ui.components.Etbo5lyAppBar

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
            // Using theme surface color for dialog background
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = stringResource(R.string.remove_fav_confirm),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.remove_fav_message, meal.strMeal),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        text = stringResource(R.string.remove_action),
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { mealToRemove = null }) {
                    Text(
                        text = stringResource(R.string.cancel_action),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }

    // Main Screen
    Scaffold(
        topBar = {
            Etbo5lyAppBar(
                navController = navController,
                text = stringResource(R.string.favourites_title)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = stringResource(R.string.no_favourites),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(R.string.tap_heart_to_save),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                // Recipes count
                Text(
                    text = stringResource(R.string.recipes_saved, favourites.size),
                    color = MaterialTheme.colorScheme.onBackground,
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
            containerColor = MaterialTheme.colorScheme.surface
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

                // Category tag — top right, using primary color
                Text(
                    text = meal.strCategory,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
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
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Remove favourite button
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.remove_action),
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}