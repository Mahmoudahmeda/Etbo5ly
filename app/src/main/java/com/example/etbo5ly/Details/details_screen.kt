package com.example.etbo5ly

import android.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.etbo5ly.Details.detailsScreenViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    recipeId: String?,
    viewmodel: detailsScreenViewModel = viewModel()
) {

    val mealData by viewmodel.meal.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val isEmbeddable by viewmodel.isVideoEmbeddable.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(recipeId) {
        viewmodel.getMeal(recipeId)
    }

    mealData?.meals?.firstOrNull()?.let { meal ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF13171F))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Recipe Details",
                        color = Color.White,
                        fontSize = 20.sp,
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
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = meal.strMeal,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ingredients",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${meal.ingredients.size} items",
                        color = Color.Cyan,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                meal.ingredients.forEach { (ingredient, amount) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = ingredient, color = Color.White, fontSize = 16.sp)
                        Text(text = amount, color = Color.Gray, fontSize = 14.sp)
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Instructions",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))

                meal.strInstructions.split("\r\n", "\n")
                    .map { it.trim() }
                    .filter { it.isNotBlank() && !it.startsWith("step", ignoreCase = true) }
                    .forEachIndexed { index, step ->
                        Row(
                            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = Color.Black,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Cyan, CircleShape),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = step,
                                color = Color.White,
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }

                Spacer(Modifier.height(24.dp))

                if (!meal.strYoutube.isNullOrBlank()) {
                    val videoId = viewmodel.getVideoId(meal.strYoutube)
                    if (isEmbeddable) {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            factory = { context ->
                                YouTubePlayerView(context).apply {
                                    lifecycleOwner.lifecycle.addObserver(this)
                                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            youTubePlayer.cueVideo(videoId, 0f)
                                        }

                                        override fun onError(
                                            youTubePlayer: YouTubePlayer,
                                            error: PlayerConstants.PlayerError
                                        ) {
                                            viewmodel.setVideoLoadingError()
                                        }
                                    })
                                }
                            }
                        )
                    } else {
                        Button(
                            onClick = {
                                viewmodel.getYoutubeIntent(meal.strYoutube).let{intent ->
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                                Spacer(Modifier.width(8.dp))
                                Text("Open in YouTube", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}