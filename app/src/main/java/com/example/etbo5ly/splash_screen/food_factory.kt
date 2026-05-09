package com.example.etbo5ly.splash_screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.etbo5ly.R
import kotlinx.coroutines.delay

@Composable
fun Food_factory(modifier: Modifier, navController: NavController, destination:String){
    val splash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.factory))
    
    LaunchedEffect(Unit) {
        delay(5000L)
        navController.navigate(destination) {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    // Uses dynamic theme background to ensure a smooth transition to the Login/Home screens
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            modifier = modifier,
            composition = splash,
            speed = 2f,
            iterations = LottieConstants.IterateForever
        )
    }
}
