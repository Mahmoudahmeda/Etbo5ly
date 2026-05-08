package com.example.etbo5ly.favourite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.data.dto.MealX
import com.example.etbo5ly.data.local.FavouritesDataStore
import com.example.etbo5ly.data.repository.CalendarRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(
    context: Context,
    private val calendarepo: CalendarRepository,
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val dataStore = FavouritesDataStore(context)
    private val _favourites = MutableStateFlow<List<MealX>>(emptyList())
    val favourites = _favourites

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            auth.currentUser?.let { calendarepo.getFavorites(it.uid) }?.collect { favorites ->
                _favourites.value = favorites.map { favorite ->
                    MealX(
                        idMeal = favorite.recipeId,
                        strMeal = favorite.recipeName,
                        strMealThumb = favorite.recipeImage,
                        strArea = favorite.recipeArea,
                        strCategory = favorite.recipeCategory,
                        strInstructions = favorite.recipeInstructions,
                        strTags = favorite.recipeTags,
                        strYoutube = favorite.recipeYoutube,
                        ingredients = favorite.recipeIngredients.split(",").mapNotNull {
                            val parts = it.split(":")
                            if (parts.size >= 2) {
                                parts[0] to parts[1]
                            } else {
                                null
                            }
                        }
                    )
                }.toList()
            }
        }
    }

    fun removeFavourite(meal: MealX) {
        viewModelScope.launch {
            auth.currentUser?.let { calendarepo.deleteFavorite(it.uid, meal.idMeal) }
        }
    }
}

class FavouritesViewModelFactory(
    private val context: Context,
    private val calendarepo: CalendarRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavouritesViewModel(context, calendarepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

