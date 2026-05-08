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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepo = AuthenticationRepo()
    private val settingsManager = SettingsManager(application)
    private val notificationHelper = NotificationHelper(application)

    // Observe the userId reactively
    private val userIdFlow = authRepo.getUserIdFlow()
    private val currentUserId: String get() = authRepo.getCurrentUserUid()

    val isNotificationsEnabled: StateFlow<Boolean> = userIdFlow
        .flatMapLatest { uid -> settingsManager.isNotificationsEnabled(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val userPhotoUrl: StateFlow<String?> = userIdFlow
        .flatMapLatest { uid ->
            settingsManager.getProfilePhoto(uid).map { localPhoto ->
                localPhoto ?: authRepo.getCurrentUserPhotoUrl()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), authRepo.getCurrentUserPhotoUrl())

    val isDarkTheme: StateFlow<Boolean> = userIdFlow
        .flatMapLatest { uid -> settingsManager.isDarkTheme(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val currentLanguage: StateFlow<String> = userIdFlow
        .flatMapLatest { uid -> settingsManager.getLanguage(uid) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setNotificationsEnabled(currentUserId, enabled)
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
            settingsManager.setDarkTheme(currentUserId, isDark)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            settingsManager.setLanguage(currentUserId, languageCode)
        }
    }

    fun updateProfilePhoto(uri: Uri) {
        viewModelScope.launch {
            try {
                val uid = currentUserId
                val fileName = "profile_${uid}.jpg"
                val file = File(getApplication<Application>().filesDir, fileName)

                getApplication<Application>().contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                }

                settingsManager.setProfilePhoto(uid, file.absolutePath)
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
}
