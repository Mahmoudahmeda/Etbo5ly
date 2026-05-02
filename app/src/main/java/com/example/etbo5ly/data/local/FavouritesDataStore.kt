package com.example.etbo5ly.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.etbo5ly.data.dto.Meal
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "favourites")

class FavouritesDataStore(private val context: Context) {

    private val gson = Gson()
    private val FAVOURITES_KEY = stringSetPreferencesKey("favourite_meals")

    val favouriteMeals: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[FAVOURITES_KEY] ?: emptySet()
        }

    suspend fun addFavourite(meal: Meal) {
        val mealJson = gson.toJson(meal)
        context.dataStore.edit { preferences ->
            val current = preferences[FAVOURITES_KEY] ?: emptySet()
            preferences[FAVOURITES_KEY] = current + mealJson
        }
    }

    suspend fun removeFavourite(meal: Meal) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVOURITES_KEY] ?: emptySet()
            preferences[FAVOURITES_KEY] = current.filter { json ->
                val saved = gson.fromJson(json, Meal::class.java)
                saved.idMeal != meal.idMeal
            }.toSet()
        }
    }
}