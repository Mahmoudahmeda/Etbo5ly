package com.example.etbo5ly.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.etbo5ly.settings.components.*
import com.example.etbo5ly.ui.components.Etbo5lyAppBar
import com.example.etbo5ly.ui.theme.*

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val viewModel: SettingsViewModel = viewModel()

    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()
    val userPhotoUrl by viewModel.userPhotoUrl.collectAsState()
    val scrollState = rememberScrollState()

    // Photo Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.updateProfilePhoto(it) }
    }

    // Notification Permission Launcher (for Android 13+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleNotifications(true)
        }
    }

    var showAboutDialog by remember { mutableStateOf(false) }

    if (showAboutDialog) {
        AboutDialog(
            onDismiss = { showAboutDialog = false },
            appVersion = viewModel.getAppVersion()
        )
    }

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
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileHeader(
                name = viewModel.getUserName(),
                email = viewModel.getUserEmail(),
                photoUrl = userPhotoUrl,
                clickOnPhoto = { photoPickerLauncher.launch("image/*") }
            )
            Spacer(modifier = Modifier.height(32.dp))

            SettingsSectionTitle("COMMUNICATION")
            SettingsGroup {
                SettingsToggleItem(
                    title = "Notifications",
                    subtitle = "Recipe alerts and meal reminders",
                    icon = Icons.Default.Notifications,
                    checked = isNotificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.toggleNotifications(enabled)
                        }
                    }
                )
            }

            SettingsSectionTitle("APP INFORMATION")
            SettingsGroup {
                SettingsClickableItem(
                    title = "About Etbo5ly",
                    subtitle = "Version ${viewModel.getAppVersion()}",
                    icon = Icons.Default.Info,
                    onClick = { showAboutDialog = true }
                )
            }

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
