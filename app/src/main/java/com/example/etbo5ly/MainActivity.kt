package com.example.etbo5ly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.etbo5ly.dashboard_screen.DashboardScreen
import com.example.etbo5ly.splash_screen.Food_factory
import com.example.etbo5ly.ui.dashboard.DashboardScreen
import com.example.etbo5ly.ui.theme.Etbo5lyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Etbo5lyTheme {
                DashboardScreen()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DashboardScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
//                    Food_factory(
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}