package com.example.etbo5ly.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.etbo5ly.settings.components.*
import com.example.etbo5ly.ui.components.Etbo5lyAppBar
import com.example.etbo5ly.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SettingsScreen(
    viewModel: ISettingsViewModel,
    navController: NavController
) {
    // Collecting states from ViewModel
    val isSyncEnabled by viewModel.isSyncEnabled.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Etbo5lyAppBar(
                navController = navController,
                text = "Profile"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Profile Header Section
            Spacer(modifier = Modifier.height(24.dp))
            ProfileHeader(
                name = viewModel.getUserName(),
                email = viewModel.getUserEmail(),
                photoUrl = viewModel.getUserPhotoUrl()
            )
            Spacer(modifier = Modifier.height(32.dp))

            // 2. Account Preferences
            SettingsSectionTitle("ACCOUNT PREFERENCES")
            SettingsGroup {
                SettingsToggleItem(
                    title = "Sync Account",
                    subtitle = "Keep recipes updated across devices",
                    icon = Icons.Default.Sync,
                    checked = isSyncEnabled,
                    onCheckedChange = { viewModel.toggleSync(it) }
                )
            }

            // 3. Communication
            SettingsSectionTitle("COMMUNICATION")
            SettingsGroup {
                SettingsToggleItem(
                    title = "Notifications",
                    subtitle = "Recipe alerts and meal reminders",
                    icon = Icons.Default.Notifications,
                    checked = isNotificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications(it) }
                )
            }

            // 4. App Information
            SettingsSectionTitle("APP INFORMATION")
            SettingsGroup {
                SettingsClickableItem(
                    title = "About Etbo5ly",
                    subtitle = "Version 1.0.0",
                    icon = Icons.Default.Info
                )
            }

            // 5. Session
            SettingsSectionTitle("SESSION")
            SettingsGroup {
                SettingsClickableItem(
                    title = "Log Out",
                    icon = Icons.Default.ExitToApp,
                    titleColor = Color.Red,
                    iconColor = Color.Red,
                    showChevron = true,
                    onClick = {
                        viewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

