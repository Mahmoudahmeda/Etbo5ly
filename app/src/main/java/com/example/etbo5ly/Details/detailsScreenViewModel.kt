package com.example.etbo5ly.Details

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.etbo5ly.data.dto.MealX
import com.example.etbo5ly.data.local.Etbo5lyDataBase
import com.example.etbo5ly.data.local.MealDao
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import com.example.etbo5ly.data.repository.CalendarRepo
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.data.repository.MealRepository
import com.example.etbo5ly.notifications.MealNotificationWorker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class detailsScreenViewModel(
    application: Application,
    private val repository: CalendarRepository
) : AndroidViewModel(application) {
    private val api = ApiClient.service
    private val datastore = RemoteDataSource(api)
    private val _Meal = MutableStateFlow<MealDetails?>(null)
    val meal = _Meal
    private val auth = FirebaseAuth.getInstance()
    private val _favouriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favouriteIds: StateFlow<Set<String>> = _favouriteIds

    init {
        loadFavouriteIds()
    }
    private fun loadFavouriteIds() {
        viewModelScope.launch {
            auth.currentUser?.let { repository.getFavorites(it.uid) }?.collect { favorites ->
                _favouriteIds.value = favorites.map { it.recipeId }.toSet()
            }
        }
    }
    fun getMeal(id: String?) {
        viewModelScope.launch {
            val response = datastore.getMealDetails(id)
            if (response.isSuccessful) {
                _Meal.value = response.body()
            }
        }
    }

    fun addToCalendar(recipe: MealX, selectedTimestamp: Long) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val calendarEntry = CalendarEntity(
                userId = userId,
                mealId = recipe.idMeal,
                mealName = recipe.strMeal,
                mealImage = recipe.strMealThumb,
                date = selectedTimestamp
            )
            repository.insertMealToCalendar(calendarEntry)
        }
    }

    fun onFavoriteClick(meal: MealX) {
        viewModelScope.launch {
            if (_favouriteIds.value.contains(meal.idMeal)) {
                auth.currentUser?.let { repository.deleteFavorite(it.uid, meal.idMeal) }
            } else {
                auth.currentUser?.let { repository.insertFavorite(FavoriteEntity(
                    userId = it.uid,
                    recipeId = meal.idMeal,
                    recipeName = meal.strMeal,
                    recipeImage = meal.strMealThumb,
                    recipeInstructions = meal.strInstructions,
                    recipeIngredients = meal.ingredients.joinToString(","),
                    recipeTags = meal.strTags ?: "",
                    recipeCategory = meal.strCategory,
                    recipeArea = meal.strArea,
                    recipeYoutube = meal.strYoutube ?: ""
                )) }
            }
        }
    }
    fun scheduleMealNotification(recipeName: String, scheduledTime: Long) {
        val currentTime = System.currentTimeMillis()
        val delay = scheduledTime - currentTime

        if (delay > 0) {
            // Bundle the recipe name to show it in the notification later
            val data = workDataOf("RECIPE_NAME" to recipeName)

            val workRequest = OneTimeWorkRequestBuilder<MealNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("meal_work_$recipeName") // Helpful if you want to cancel it later
                .build()

            WorkManager.getInstance(getApplication()).enqueue(workRequest)
        }
    }
}

