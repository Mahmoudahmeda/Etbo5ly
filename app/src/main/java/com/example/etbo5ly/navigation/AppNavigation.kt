package com.example.etbo5ly.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.etbo5ly.authentication.Signup
import com.example.etbo5ly.authentication.signin.Signin
import com.example.etbo5ly.authentication.signin.Signin_screen
import com.example.etbo5ly.splash_screen.Food_factory

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavigation(modifier: Modifier){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash"){
            Food_factory(modifier,navController,Signin())
        }
        composable("login"){
            Signin_screen(Signin(), navController)
        }
        composable("signup"){
            Signup(modifier)
        }
        composable("home"){
            Signup(modifier)
        }
    }
}