package com.example.etbo5ly.Details

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.core.net.toUri
import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealX
import com.example.etbo5ly.data.local.FavouritesDataStore
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import com.example.etbo5ly.data.dto.Meal
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class detailsScreenViewModel(private val context: Context) : ViewModel() {

    private val api = ApiClient.service
    private val datastore = RemoteDataSource(api)
    private val favouritesDataStore = FavouritesDataStore(context)
    private val gson = Gson()

    private val _meal = MutableStateFlow<MealDetails?>(null)
    val meal: StateFlow<MealDetails?> = _meal

    private val _isOfflineAndNotFavourited = MutableStateFlow(false)
    val isOfflineAndNotFavourited: StateFlow<Boolean> = _isOfflineAndNotFavourited

    fun getMeal(id: String?, isOnline: Boolean) {
        viewModelScope.launch {
            if (isOnline) {
                // Online fetch from API
                val response = datastore.getMealDetails(id)
                if (response.isSuccessful) {
                    _meal.value = response.body()
                }
            } else {
                // Offline try to find in favourites
                val savedMeal = findInFavourites(id)
                if (savedMeal != null) {
                    _meal.value = MealDetails(meals = listOf(convertToMealX(savedMeal)))
                } else {
                    _isOfflineAndNotFavourited.value = true
                }
            }
        }
    }

    private suspend fun findInFavourites(mealId: String?): Meal? {
        if (mealId == null) return null
        val jsonSet = favouritesDataStore.favouriteMeals.first()
        return jsonSet.mapNotNull { json ->
            runCatching { gson.fromJson(json, Meal::class.java) }.getOrNull()
        }.find { it.idMeal == mealId }
    }

    private fun convertToMealX(meal: Meal): MealX {
        val ingredients = (1..20).mapNotNull { i ->
            val ingredient = when (i) {
                1 -> meal.strIngredient1
                2 -> meal.strIngredient2
                3 -> meal.strIngredient3
                4 -> meal.strIngredient4
                5 -> meal.strIngredient5
                6 -> meal.strIngredient6
                7 -> meal.strIngredient7
                8 -> meal.strIngredient8
                9 -> meal.strIngredient9
                10 -> meal.strIngredient10
                11 -> meal.strIngredient11
                12 -> meal.strIngredient12
                13 -> meal.strIngredient13
                14 -> meal.strIngredient14
                15 -> meal.strIngredient15
                16 -> meal.strIngredient16
                17 -> meal.strIngredient17
                18 -> meal.strIngredient18
                19 -> meal.strIngredient19
                else -> meal.strIngredient20
            }
            val measure = when (i) {
                1 -> meal.strMeasure1
                2 -> meal.strMeasure2
                3 -> meal.strMeasure3
                4 -> meal.strMeasure4
                5 -> meal.strMeasure5
                6 -> meal.strMeasure6
                7 -> meal.strMeasure7
                8 -> meal.strMeasure8
                9 -> meal.strMeasure9
                10 -> meal.strMeasure10
                11 -> meal.strMeasure11
                12 -> meal.strMeasure12
                13 -> meal.strMeasure13
                14 -> meal.strMeasure14
                15 -> meal.strMeasure15
                16 -> meal.strMeasure16
                17 -> meal.strMeasure17
                18 -> meal.strMeasure18
                19 -> meal.strMeasure19
                else -> meal.strMeasure20
            }
            if (!ingredient.isNullOrBlank()) ingredient to (measure ?: "")
            else null
        }

        return MealX(
            idMeal = meal.idMeal,
            strMeal = meal.strMeal,
            strCategory = meal.strCategory,
            strArea = meal.strArea,
            strInstructions = meal.strInstructions,
            strMealThumb = meal.strMealThumb,
            strTags = meal.strTags?.toString(),
            strYoutube = meal.strYoutube,
            ingredients = ingredients
        )
    }

    fun getVideoId(url: String?): String {
        return url?.substringAfter("v=", "")?.substringBefore("&") ?: ""
    }

    fun getYoutubeIntent(url: String?): Intent? {
        return if (!url.isNullOrBlank()) {
            Intent(Intent.ACTION_VIEW, url.toUri())
        } else null
    }
}

class detailsScreenViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(detailsScreenViewModel::class.java)) {
            return detailsScreenViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}