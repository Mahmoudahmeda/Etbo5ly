package com.example.etbo5ly


import android.app.Application
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
import android.widget.Toast
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.etbo5ly.Details.DetailsViewModelFactory
import com.example.etbo5ly.calendar.CalendarViewModel
import com.example.etbo5ly.calendar.CalendarViewModelFactory
import com.example.etbo5ly.data.local.Etbo5lyDataBase
import com.example.etbo5ly.data.repository.CalendarRepository
import java.util.Calendar

// Inter font sizes — all in range 14-22sp
private val TitleSize = 22.sp
private val SectionSize = 18.sp
private val BodySize = 16.sp
private val SmallSize = 14.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    recipeId: String?
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val database = remember { Etbo5lyDataBase.getDataBase(context) }
    val repository = remember { CalendarRepository(database.mealDao()) }

    // Create the ViewModel with the factory to avoid RuntimeException
    val viewmodel: detailsScreenViewModel = viewModel(
        factory = DetailsViewModelFactory(application = application,repo=repository)
    )
    val mealData by viewmodel.meal.collectAsState()

    // Instructions expand/collapse state
    var instructionsExpanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val favouriteIds by viewmodel.favouriteIds.collectAsState()


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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF13171F))
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {

                // ── Meal Image ──────────────────────────────────────────
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

                // ── Meal Name & Add to Plan ─────────────────────────────
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meal.strMeal,
                        color = Color.White,
                        fontSize = TitleSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    AddToFavoriteSection(
                        meal,
                        onFavClick = {
                            viewmodel.onFavoriteClick(meal)
                            val message = if (favouriteIds.contains(meal.idMeal))
                                "Removed from favourites"
                            else
                                "Added to favourites"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        },
                        isFavorite = favouriteIds.contains(meal.idMeal)
                    )
                    AddToCalendarSection(meal, viewModel = viewmodel){ selectedTimestamp ->
                        viewmodel.scheduleMealNotification(meal.strMeal, selectedTimestamp)
                        viewmodel.addToCalendar(meal, selectedTimestamp)
                    }
                }
                
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

                // ── Ingredients List with Icons ─────────────────────────
                meal.ingredients.forEach { (ingredient, amount) ->
                    IngredientRow(ingredient = ingredient, amount = amount, navController)
                }

                Spacer(Modifier.height(24.dp))

                // ── Instructions Header ─────────────────────────────────
                Text(
                    text = "Instructions",
                    color = Color.White,
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
                        // Step number circle
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

                // ── View More / View Less ───────────────────────────────
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

                // ── Watch Recipe / YouTube ──────────────────────────────
                if (!meal.strYoutube.isNullOrBlank()) {
                    Text(
                        text = "Watch Tutorial",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = SectionSize,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(12.dp))
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
                    .background(Color(0xFF232832))
            )
            Text(
                text = ingredient,
                color = Color.White,
                fontSize = BodySize
            )
        }
        Text(
            text = amount,
            color = Color.Gray,
            fontSize = SmallSize
        )
    }
}

// ── Tag Chip ────────────────────────────────────────────────────────────────
@Composable
private fun TagChip(label: String,navController: NavController,meal: MealX) {
    Text(
        text = label,
        color = Color.Cyan,
        fontSize = SmallSize,
        modifier = Modifier
            .background(
                color = Color.Cyan.copy(alpha = 0.15f),
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoId = remember(videoUrl) {
        if (videoUrl.contains("v=")) {
            videoUrl.split("v=")[1].split("&")[0]
        } else if (videoUrl.contains("youtu.be/")) {
            videoUrl.split("/").last().split("?")[0]
        } else {
            videoUrl.split("/").last()
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp)),
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        },
        onRelease = {
            it.release()
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCalendarSection(
    recipe: MealX,
    viewModel: detailsScreenViewModel,
    onAddClicked: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    val datePickerState = rememberDatePickerState()

    // 1. The Add Button
    IconButton(
        onClick = { showDatePicker = true },
        modifier = Modifier.padding(10.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF00BCD4)), // Cyan/Teal
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(Icons.Default.DateRange, contentDescription = "Calendar")
    }

    // 2. The Material 3 Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Next") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val calendar = Calendar.getInstance()
                    // Set Date first
                    datePickerState.selectedDateMillis?.let {
                        calendar.timeInMillis = it
                    }
                    // Immediately override with local time to fix the "3 AM" bug
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                    calendar.set(Calendar.SECOND, 0)
                        // ONLY CALL THESE HERE (Once both Date and Time are ready)
                    val finalTimestamp = calendar.timeInMillis
                    viewModel.addToCalendar(recipe, finalTimestamp)
                    viewModel.scheduleMealNotification(recipe.strMeal, finalTimestamp)
                    showTimePicker = false
                }) { Text("Schedule") } },
            { Text("Schedule") }
        ) { TimePicker(state = timePickerState) }
    }
}
@Composable
fun AddToFavoriteSection(
    recipe: MealX,
    onFavClick : ()-> Unit,
    isFavorite : Boolean
) {
    // 1. The Add Button
    IconButton(
        onClick = {
            onFavClick()
        },
        modifier = Modifier.padding(10.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF00BCD4)), // Cyan/Teal
        shape = RoundedCornerShape(10.dp)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite
            else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) Color.Red else Color.White
        )
    }
}

