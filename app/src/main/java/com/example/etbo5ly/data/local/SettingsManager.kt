package com.example.etbo5ly.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    private fun notifyKey(userId: String) = booleanPreferencesKey("${userId}_notifications")
    private fun photoKey(userId: String) = stringPreferencesKey("${userId}_photo_url")

    fun isNotificationsEnabled(userId: String): Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { it[notifyKey(userId)] ?: true }

    suspend fun setNotificationsEnabled(userId: String, enabled: Boolean) {
        try {
            context.dataStore.edit { it[notifyKey(userId)] = enabled }
        } catch (e: IOException) {
            Log.e("SettingsManager", "Error writing data: ${e.message}")
        }
    }

    fun getProfilePhoto(userId: String): Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { it[photoKey(userId)] }

    suspend fun setProfilePhoto(userId: String, url: String) {
        try {
            context.dataStore.edit { it[photoKey(userId)] = url }
        } catch (e: IOException) {
            Log.e("SettingsManager", "Error writing data: ${e.message}")
        }
    }
}
