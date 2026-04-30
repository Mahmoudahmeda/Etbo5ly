package com.example.etbo5ly.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.local.SettingsManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Interface to allow Preview and testing
interface ISettingsViewModel {
    val isSyncEnabled: StateFlow<Boolean>
    val isNotificationsEnabled: StateFlow<Boolean>
    val isOfflineEnabled: StateFlow<Boolean>
    fun toggleSync(enabled: Boolean)
    fun toggleNotifications(enabled: Boolean)
    fun toggleOffline(enabled: Boolean)
    fun signOut()
    fun getUserEmail(): String
    fun getUserName(): String
    fun getUserPhotoUrl(): String?
}

class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel(), ISettingsViewModel {

    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid ?: "guest"

    // Expose flows from DataStore as StateFlow for Compose
    override val isSyncEnabled: StateFlow<Boolean> = settingsManager.isSyncEnabled(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    override val isNotificationsEnabled: StateFlow<Boolean> = settingsManager.isNotificationsEnabled(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    override val isOfflineEnabled: StateFlow<Boolean> = settingsManager.isOfflineEnabled(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // UI Actions
    override fun toggleSync(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setSyncEnabled(userId, enabled)
        }
    }

    override fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setNotificationsEnabled(userId, enabled)
        }
    }

    override fun toggleOffline(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setOfflineEnabled(userId, enabled)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getUserEmail() = auth.currentUser?.email ?: "No Email"
    override fun getUserName() = auth.currentUser?.displayName ?: "User"
    override fun getUserPhotoUrl() = auth.currentUser?.photoUrl?.toString()
}
