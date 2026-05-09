package com.example.etbo5ly.Details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealX
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.notifications.MealNotificationWorker
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class detailsScreenViewModel(
    application: Application,
    private val repository: CalendarRepository
) : AndroidViewModel(application) {
    private val api = ApiClient.service
    private val datastore = RemoteDataSource(api)
    private val auth = FirebaseAuth.getInstance()
    private val _favouriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favouriteIds: StateFlow<Set<String>> = _favouriteIds
    private val _isOfflineAndNotFavourited = MutableStateFlow(false)
    val isOfflineAndNotFavourited: StateFlow<Boolean> = _isOfflineAndNotFavourited
    private val _Meal = MutableStateFlow<MealDetails?>(null)
    val meal = _Meal

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
    fun getMeal(id: String?, isOnline: Boolean) {
        viewModelScope.launch {
            if (isOnline) {
                // Online fetch from API
                val response = datastore.getMealDetails(id)
                if (response.isSuccessful) {
                    _Meal.value = response.body()
                }
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

class detailsScreenViewModelFactory(
    private val application: Application,
    private val repo: CalendarRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(detailsScreenViewModel::class.java)) {
            return detailsScreenViewModel(application, repository = repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}