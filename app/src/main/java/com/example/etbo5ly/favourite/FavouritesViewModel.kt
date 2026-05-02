package com.example.etbo5ly.favourite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.data.local.FavouritesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(private val context: Context) : ViewModel() {

    private val dataStore = FavouritesDataStore(context)
    private val gson = Gson()

    private val _favourites = MutableStateFlow<List<Meal>>(emptyList())
    val favourites: StateFlow<List<Meal>> = _favourites

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            dataStore.favouriteMeals.collect { jsonSet ->
                _favourites.value = jsonSet.mapNotNull { json ->
                    runCatching { gson.fromJson(json, Meal::class.java) }.getOrNull()
                }
            }
        }
    }

    fun removeFavourite(meal: Meal) {
        viewModelScope.launch {
            dataStore.removeFavourite(meal)
        }
    }
}

class FavouritesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            return FavouritesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}