package com.example.etbo5ly.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.etbo5ly.R
import kotlinx.coroutines.delay

@Composable
fun Food_factory(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit
) {
    val splash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.factory))


    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF261411)),
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