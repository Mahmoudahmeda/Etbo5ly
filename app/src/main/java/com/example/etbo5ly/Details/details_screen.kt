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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.etbo5ly.Details.detailsScreenViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.example.etbo5ly.data.dto.MealX
import android.util.Log
import com.example.etbo5ly.ui.components.Etbo5lyAppBar

// Inter font sizes — all in range 14-22sp
private val TitleSize = 22.sp
private val SectionSize = 18.sp
private val BodySize = 16.sp
private val SmallSize = 14.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    recipeId: String?,
    viewmodel: detailsScreenViewModel = viewModel()
) {
    val mealData by viewmodel.meal.collectAsState()


    // Instructions expand/collapse state
    var instructionsExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(recipeId) {
        viewmodel.getMeal(recipeId)
    }

    mealData?.meals?.firstOrNull()?.let { meal ->

        // Parse instructions into clean steps
        val allSteps = meal.strInstructions
            .split("\r\n", "\n")
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.startsWith("step", ignoreCase = true) }

        // Show only first 2 steps when collapsed
        val visibleSteps = if (instructionsExpanded) allSteps else allSteps.take(2)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Etbo5lyAppBar(
                    navController = navController,
                    text = "Recipe Details"
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {

                Spacer(Modifier.height(16.dp))

                // Meal Image
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                Spacer(Modifier.height(16.dp))

                // ── Meal Name ───────────────────────────────────────────
                Text(
                    text = meal.strMeal,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = TitleSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(8.dp))

                // ── Category + Area Tags ────────────────────────────────
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TagChip(meal.strCategory, navController, meal)
                    TagChip(meal.strArea, navController, meal)
                }

                Spacer(Modifier.height(24.dp))

                // ── Ingredients Header ──────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ingredients",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = SectionSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${meal.ingredients.size} items",
                        color = MaterialTheme.colorScheme.primary, // Turquoise or Red
                        fontSize = SmallSize
                    )
                }

                Spacer(Modifier.height(12.dp))

                // ── Ingredients List with Icons ─────────────────────────
                meal.ingredients.forEach { (ingredient, amount) ->
                    IngredientRow(ingredient = ingredient, amount = amount, navController)
                }

                Spacer(Modifier.height(24.dp))

                // ── Instructions Header ─────────────────────────────────
                Text(
                    text = "Instructions",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = SectionSize,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))

                // ── Instructions Steps ──────────────────────────────────
                visibleSteps.forEachIndexed { index, step ->
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Step number circle using Primary color
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = SmallSize,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = step,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = BodySize,
                            lineHeight = 22.sp
                        )
                    }
                }

                // ── View More / View Less ───────────────────────────────
                if (allSteps.size > 2) {
                    Text(
                        text = if (instructionsExpanded) "View Less ↑" else "View More ↓",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = SmallSize,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { instructionsExpanded = !instructionsExpanded }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // ── Watch Recipe / YouTube ──────────────────────────────
                if (!meal.strYoutube.isNullOrBlank()) {
                    YoutubePlayer(meal.strYoutube)
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// ── Ingredient Row with API icon ────────────────────────────────────────────
@Composable
private fun IngredientRow(ingredient: String, amount: String,navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable{
                navController.navigate("searchResult/ingredient/${ingredient}")
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ingredient icon from MealDB API
            AsyncImage(
                model = "https://www.themealdb.com/images/ingredients/${ingredient}-Small.png",
                contentDescription = ingredient,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Matches the warm/cool variant
            )
            Text(
                text = ingredient,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = BodySize
            )
        }
        Text(
            text = amount,
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Muted text color for measurements
            fontSize = SmallSize
        )
    }
}

// ── Tag Chip ────────────────────────────────────────────────────────────────
@Composable
private fun TagChip(label: String,navController: NavController,meal: MealX) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.primary,
        fontSize = SmallSize,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable{
                if(label.equals(meal.strCategory)){
                    navController.navigate("searchResult/category/${meal.strCategory}")
                }else{
                    navController.navigate("searchResult/country/${meal.strArea}")
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