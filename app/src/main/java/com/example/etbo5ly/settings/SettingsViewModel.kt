package com.example.etbo5ly.settings

import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.authentication.AuthenticationRepo
import com.example.etbo5ly.data.local.SettingsManager
import com.example.etbo5ly.notifications.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepo = AuthenticationRepo()
    private val settingsManager = SettingsManager(application)
    private val notificationHelper = NotificationHelper(application)
    private val userId = authRepo.getCurrentUserUid()

    val isNotificationsEnabled: StateFlow<Boolean> = settingsManager.isNotificationsEnabled(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val userPhotoUrl: StateFlow<String?> = settingsManager.getProfilePhoto(userId)
        .map { localPhoto ->
            localPhoto ?: authRepo.getCurrentUserPhotoUrl()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), authRepo.getCurrentUserPhotoUrl())

    val isDarkTheme: StateFlow<Boolean> = settingsManager.isDarkTheme(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true) // Default to Dark mode

    private val _Language = MutableStateFlow("en")
    val language = _Language
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setNotificationsEnabled(userId, enabled)
            if (enabled) {
                notificationHelper.showNotification(
                    "Notifications Enabled",
                    "You will now receive recipe alerts and meal reminders."
                )
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsManager.setDarkTheme(userId, isDark)
        }
    }

    fun updateProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            try {
                // Create a local file to store the image
                val fileName = "profile_${userId}.jpg"
                val file = File(getApplication<Application>().filesDir, fileName)
                
                // Copy the image data from the Uri to the local file
                getApplication<Application>().contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                // Save the local file path to DataStore
                settingsManager.setProfilePhoto(userId, file.absolutePath)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving profile photo: ${e.message}")
            }
        }
    }

    fun signOut() {
        authRepo.signOut()
    }

    fun getUserEmail() = authRepo.getCurrentUserEmail()
    fun getUserName() = authRepo.getCurrentUserName()
    
    fun getAppVersion(): String {
        return try {
            val context = getApplication<Application>()
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            packageInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }
    fun changeLanguage() {
        viewModelScope.launch {
            try {
                val language = if (_Language.value == "en") {
                    _Language.value = "ar"
                    "ar"
                } else {
                    _Language.value = "en"
                    "en"
                }
                val currentLocale = LocaleListCompat.forLanguageTags(language)
                AppCompatDelegate.setApplicationLocales(currentLocale)
            }catch (e: Exception){
                Log.e("SettingsViewModel", "Error changing language: ${e.message}")
            }
        }
    }
}
