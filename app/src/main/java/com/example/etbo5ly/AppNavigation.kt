package com.example.etbo5ly

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.etbo5ly.splash_screen.Food_factory

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {

        composable(route = "splash") {
            Food_factory(
                onTimeout = {
                    navController.navigate("signup") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "signup") {
            val signUpViewModel: SignUpViewModel = viewModel()
            SignUpScreen(
                viewModel = signUpViewModel,
                onNavigateToHome = {
                    navController.navigate("home")
                }
            )
        }

        composable(route = "home") {
            Text(text = "Welcome Home - Time to cook!")
        }
    }
}