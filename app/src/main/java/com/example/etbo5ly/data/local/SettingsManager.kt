package com.example.etbo5ly.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    private fun syncKey(userId: String) = booleanPreferencesKey("${userId}_sync")
    private fun notifyKey(userId: String) = booleanPreferencesKey("${userId}_notifications")
    private fun offlineKey(userId: String) = booleanPreferencesKey("${userId}_offline")

    fun isSyncEnabled(userId: String): Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) 
            else throw exception
        }.map { it[syncKey(userId)] ?: true }

    fun isNotificationsEnabled(userId: String): Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) 
            else throw exception
        }.map { it[notifyKey(userId)] ?: true }

    fun isOfflineEnabled(userId: String): Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) 
            else throw exception
        }.map { it[offlineKey(userId)] ?: false }

    suspend fun setSyncEnabled(userId: String, enabled: Boolean) {
        try {
            context.dataStore.edit { it[syncKey(userId)] = enabled }
        } catch (e: IOException) {
            Log.e("SettingsManager", "Error writing data: ${e.message}")
        }
    }

    suspend fun setNotificationsEnabled(userId: String, enabled: Boolean) {
        try {
            context.dataStore.edit { it[notifyKey(userId)] = enabled }
        } catch (e: IOException) {
            Log.e("SettingsManager", "Error writing data: ${e.message}")
        }
    }

    suspend fun setOfflineEnabled(userId: String, enabled: Boolean) {
        try {
            context.dataStore.edit { it[offlineKey(userId)] = enabled }
        } catch (e: IOException) {
            Log.e("SettingsManager", "Error writing data: ${e.message}")
        }
    }
}
