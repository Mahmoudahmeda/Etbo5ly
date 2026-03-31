package com.example.etbo5ly.splash_screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.etbo5ly.R
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.airbnb.lottie.compose.LottieConstants
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.authentication.signin.Signin
import kotlinx.coroutines.delay


@Composable
fun Food_factory(modifier: Modifier, navController: NavController, signin: Signin){
    val splash by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.factory))
    LaunchedEffect(Unit) {
        delay(5000L)
        navController.navigate(signin.isLoggedDestination) {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }
    Column(modifier = Modifier.background(color = Color(0xFF261411))) {
        LottieAnimation(
            modifier = modifier,
            composition = splash,
            speed = 2f,
            iterations = LottieConstants.IterateForever
        )
    }
}