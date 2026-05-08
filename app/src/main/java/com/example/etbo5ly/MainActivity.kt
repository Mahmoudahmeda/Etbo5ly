package com.example.etbo5ly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.etbo5ly.navigation.AppNavigation
import com.example.etbo5ly.settings.SettingsViewModel
import com.example.etbo5ly.ui.theme.Etbo5lyTheme
import com.example.etbo5ly.util.LocalActivity
import com.example.etbo5ly.util.LocaleUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Get settings from the ViewModel
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            val currentLanguage by settingsViewModel.currentLanguage.collectAsState()

            // Update configuration based on the selected language
            val context = LocalContext.current
            val localizedContext = remember(currentLanguage) {
                LocaleUtils.setLocale(context, currentLanguage)
            }

            // Set layout direction (RTL for Arabic)
            val layoutDirection = if (currentLanguage == "ar") {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            // Provide localized context, layout direction and the original activity to the composition tree
            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides localizedContext.resources.configuration,
                LocalLayoutDirection provides layoutDirection,
                LocalActivityResultRegistryOwner provides this,
                LocalActivity provides this // CRITICAL: Provide the original Activity context
            ) {
                Etbo5lyTheme(darkTheme = isDarkTheme) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppNavigation(Modifier.padding(innerPadding), intent)
                    }
                }
            }
        }
    }
}
