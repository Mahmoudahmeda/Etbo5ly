package com.example.etbo5ly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.etbo5ly.Details.detailsScreenViewModel
import com.example.etbo5ly.Details.detailsScreenViewModelFactory
import com.example.etbo5ly.data.dto.MealX
import com.example.etbo5ly.ui.components.NoInternetScreen
import com.example.etbo5ly.utils.isInternetAvailable
import com.example.etbo5ly.utils.observeNetworkConnectivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

private val TitleSize = 22.sp
private val SectionSize = 18.sp
private val BodySize = 16.sp
private val SmallSize = 14.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    recipeId: String?,
    viewmodel: detailsScreenViewModel = viewModel(
        factory = detailsScreenViewModelFactory(LocalContext.current)
    )
) {
    val context = LocalContext.current
    val isOnline by observeNetworkConnectivity(context)
        .collectAsState(initial = isInternetAvailable(context))

    val mealData by viewmodel.meal.collectAsState()
    val isOfflineAndNotFavourited by viewmodel.isOfflineAndNotFavourited.collectAsState()

    var instructionsExpanded by remember { mutableStateOf(false) }

    // Show snackbar state for offline click attempts
    var showOfflineSnackbar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(recipeId) {
        viewmodel.getMeal(recipeId, isOnline)
    }

    // Show snackbar when offline action attempted
    LaunchedEffect(showOfflineSnackbar) {
        if (showOfflineSnackbar) {
            snackbarHostState.showSnackbar(
                message = "No internet connection",
                duration = SnackbarDuration.Short
            )
            showOfflineSnackbar = false
        }
    }

    // Offline and meal not in favourites
    if (isOfflineAndNotFavourited) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF13171F))
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Recipe Details",
                        color = Color.White,
                        fontSize = SectionSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NoInternetScreen()
            }
        }
        return
    }

    mealData?.meals?.firstOrNull()?.let { meal ->

        val allSteps = meal.strInstructions
            .split("\r\n", "\n")
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("step", ignoreCase = true) }

        val visibleSteps = if (instructionsExpanded) allSteps else allSteps.take(2)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF1E2228),
                        contentColor = Color.White,
                        actionColor = Color.Cyan
                    )
                }
            },
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF13171F))
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Recipe Details",
                        color = Color.White,
                        fontSize = SectionSize,
                        fontWeight = FontWeight.Bold
                    )

                    // Offline indicator in top bar
                    if (!isOnline) {
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiOff,
                                contentDescription = "Offline",
                                tint = Color.Cyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Offline",
                                color = Color.Cyan,
                                fontSize = SmallSize
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF13171F))
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {

                // Meal image — Coil caches images so shows even offline
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(meal.strMealThumb)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = meal.strMeal,
                    color = Color.White,
                    fontSize = TitleSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagChip(
                        label = meal.strCategory,
                        navController = navController,
                        meal = meal,
                        isOnline = isOnline,
                        onOfflineClick = { showOfflineSnackbar = true }
                    )
                    TagChip(
                        label = meal.strArea,
                        navController = navController,
                        meal = meal,
                        isOnline = isOnline,
                        onOfflineClick = { showOfflineSnackbar = true }
                    )
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ingredients",
                        color = Color.White,
                        fontSize = SectionSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${meal.ingredients.size} items",
                        color = Color.Cyan,
                        fontSize = SmallSize
                    )
                }

                Spacer(Modifier.height(12.dp))

                meal.ingredients.forEach { (ingredient, amount) ->
                    IngredientRow(
                        ingredient = ingredient,
                        amount = amount,
                        navController = navController,
                        isOnline = isOnline,
                        onOfflineClick = { showOfflineSnackbar = true }
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Instructions",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = SectionSize,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))

                visibleSteps.forEachIndexed { index, step ->
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.Cyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = SmallSize,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = step,
                            color = Color.White,
                            fontSize = BodySize,
                            lineHeight = 22.sp
                        )
                    }
                }

                if (allSteps.size > 2) {
                    Text(
                        text = if (instructionsExpanded) "View Less ↑" else "View More ↓",
                        color = Color.Cyan,
                        fontSize = SmallSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { instructionsExpanded = !instructionsExpanded }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // YouTube only when online
                if (isOnline && !meal.strYoutube.isNullOrBlank()) {
                    Text(
                        text = "Watch Recipe",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = SectionSize,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    YoutubePlayer(meal.strYoutube)
                } else if (!isOnline && !meal.strYoutube.isNullOrBlank()) {
                    // Offline video placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1E2228))
                            .clickable { showOfflineSnackbar = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Video unavailable offline",
                                color = Color.Gray,
                                fontSize = SmallSize,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: String,
    amount: String,
    navController: NavController,
    isOnline: Boolean,
    onOfflineClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                if (isOnline) {
                    navController.navigate("searchResult/ingredient/$ingredient")
                } else {
                    onOfflineClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ingredient image — uses Coil disk cache so works offline
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://www.themealdb.com/images/ingredients/$ingredient-Small.png")
                    .crossfade(true)
                    .build(),
                contentDescription = ingredient,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF232832)),
                // Placeholder shown while loading or when offline and not cached
                error = androidx.compose.ui.res.painterResource(
                    id = android.R.drawable.ic_menu_gallery
                )
            )
            Text(
                text = ingredient,
                color = Color.White,
                fontSize = BodySize
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = amount,
                color = Color.Gray,
                fontSize = SmallSize
            )
            // Small arrow indicator — grayed out when offline
            Text(
                text = "›",
                color = if (isOnline) Color.Cyan else Color.Gray,
                fontSize = BodySize
            )
        }
    }
}

@Composable
private fun TagChip(
    label: String,
    navController: NavController,
    meal: MealX,
    isOnline: Boolean,
    onOfflineClick: () -> Unit
) {
    Text(
        text = label,
        color = if (isOnline) Color.Cyan else Color.Gray,
        fontSize = SmallSize,
        modifier = Modifier
            .background(
                color = if (isOnline)
                    Color.Cyan.copy(alpha = 0.15f)
                else
                    Color.Gray.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable {
                if (isOnline) {
                    if (label == meal.strCategory) {
                        navController.navigate("searchResult/category/${meal.strCategory}")
                    } else {
                        navController.navigate("searchResult/country/${meal.strArea}")
                    }
                } else {
                    onOfflineClick()
                }
            }
    )
}

@Composable
fun YoutubePlayer(videoUrl: String) {
    val videoId = remember(videoUrl) {
        if (videoUrl.contains("v=")) {
            videoUrl.split("v=")[1].split("&")[0]
        } else {
            videoUrl.split("/").last()
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        factory = { context ->
            YouTubePlayerView(context).apply {
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        }
    )
}