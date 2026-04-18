package com.example.etbo5ly.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.data.local.FavouritesDataStore
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.repository.IMealRepository
import com.example.etbo5ly.ui.categories.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: IMealRepository,
    private val context: Context
) : ViewModel() {

    private val favouritesDataStore = FavouritesDataStore(context)
    private val auth = FirebaseAuth.getInstance()

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    private val _recipes = MutableStateFlow<List<Meal>>(emptyList())
    val recipes: StateFlow<List<Meal>> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _favouriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favouriteIds: StateFlow<Set<String>> = _favouriteIds

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    init {
        getRandomMeal()
        getRecipes()
        getCategories()
        loadFavouriteIds()
    }

    fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getAMeal()
                if (response.isSuccessful) {
                    _meal.value = response.body()?.meals?.firstOrNull()
                } else {
                    _error.value = "Failed to load meal"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipes(count: Int = 10) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                val mealsList = mutableListOf<Meal>()
                repeat(count) {
                    val response = repository.getAMeal()
                    if (response.isSuccessful) {
                        response.body()?.meals?.firstOrNull()?.let { meal ->
                            mealsList.add(meal)
                        }
                    }
                }
                _recipes.value = mealsList
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                val response = ApiClient.mealApi.getCategories()
                _categories.value = response.categories.map { dto ->
                    Category(
                        name = dto.strCategory,
                        image = dto.strCategoryThumb
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadFavouriteIds() {
        viewModelScope.launch {
            favouritesDataStore.favouriteMeals.collect { jsonSet ->
                _favouriteIds.value = jsonSet.map { json ->
                    Gson().fromJson(json, Meal::class.java).idMeal
                }.toSet()
            }
        }
    }

    fun onFavoriteClick(meal: Meal) {
        viewModelScope.launch {
            if (_favouriteIds.value.contains(meal.idMeal)) {
                favouritesDataStore.removeFavourite(meal)
            } else {
                favouritesDataStore.addFavourite(meal)
            }
        }
    }

    fun logout() {
        auth.signOut()
        _isLoggedOut.value = true
    }
}