package com.example.etbo5ly.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.etbo5ly.RecipeDetailsScreen
import com.example.etbo5ly.authentication.signin.Signin
import com.example.etbo5ly.authentication.signin.Signin_screen
import com.example.etbo5ly.authentication.signup.SignUpScreen
import com.example.etbo5ly.authentication.signup.SignUpViewModel
import com.example.etbo5ly.splash_screen.Food_factory
import com.example.etbo5ly.dashboard_screen.DashboardScreen
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.authentication.changePassword.ChangePasswordScreen
import com.example.etbo5ly.authentication.emailVerify.EmailVerificationScreen

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavigation(modifier: Modifier,intent: Intent){
    val navController = rememberNavController()
    val repo = AuthenticationRepo()
    val data = intent.data
    val code = data?.getQueryParameter("oobCode")

    val destination = when {
        code != null -> "resetPassword/$code"
        repo.isLoggedIn() -> "home"
        else -> "login"
    }
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash"){
            Food_factory(modifier,navController,destination)
        }
        composable("login"){
            Signin_screen(Signin(), navController)
        }
        composable("signup"){
            SignUpScreen(SignUpViewModel(),navController)
        }
        composable("home"){
            DashboardScreen(navcontroller = navController)
        }
        composable("emailscreen"){
            EmailVerificationScreen()
        }
        composable("resetPassword/{Code}") { backStack ->
            val oobCode = backStack.arguments?.getString("Code")
            ChangePasswordScreen(navController, oobCode)
        }
        composable("details/{Id}"){ id ->
            val mealId = id.arguments?.getString("Id")
            RecipeDetailsScreen(navController,mealId)
        }
    }
}