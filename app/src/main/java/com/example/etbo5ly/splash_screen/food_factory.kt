package com.example.etbo5ly.splash_screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.etbo5ly.R
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieConstants


@Composable
fun Food_factory(modifier: Modifier){
    val splash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.factory))
    Column(modifier = Modifier.background(color = Color(0xFF261411))) {
        LottieAnimation(
            modifier = modifier,
            composition = splash,
            speed = 2f,
            iterations = LottieConstants.IterateForever
        )
    }
}