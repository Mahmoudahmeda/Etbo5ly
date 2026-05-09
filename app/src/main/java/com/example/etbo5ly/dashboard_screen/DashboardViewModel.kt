package com.example.etbo5ly.dashboard_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.MealX
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.data.repository.IMealRepository
import com.example.etbo5ly.ui.categories.Category
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: IMealRepository,
    private val calendarepo: CalendarRepository,
    private val isGuest: Boolean = false
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _meal = MutableStateFlow<MealX?>(null)
    val meal: StateFlow<MealX?> = _meal

    private val _recipes = MutableStateFlow<List<MealX>>(emptyList())
    val recipes: StateFlow<List<MealX>> = _recipes

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

    private val _showGuestFavouriteDialog = MutableStateFlow(false)
    val showGuestFavouriteDialog: StateFlow<Boolean> = _showGuestFavouriteDialog

    init {
        getRandomMeal()
        getRecipes()
        getCategories()
        if (!isGuest) loadFavouriteIds()
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
                val mealsList = mutableListOf<MealX>()
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
                val response = ApiClient.service.getCategories()
                response.body()?.categories?.let{
                    _categories.value = it.map { dto ->
                        Category(
                            name = dto.strCategory,
                            image = dto.strCategoryThumb
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadFavouriteIds() {
        viewModelScope.launch {
            auth.currentUser?.let { calendarepo.getFavorites(it.uid) }?.collect { favorites ->
                _favouriteIds.value = favorites.map { it.recipeId }.toSet()
            }
        }
    }

    fun onFavoriteClick(meal: MealX) {
        if (isGuest) {
            _showGuestFavouriteDialog.value = true
            return
        }
        viewModelScope.launch {
            if (_favouriteIds.value.contains(meal.idMeal)) {
                auth.currentUser?.let { calendarepo.deleteFavorite(it.uid, meal.idMeal) }
            } else {
                auth.currentUser?.let { calendarepo.insertFavorite(FavoriteEntity(
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

    fun showGuestDialog() {
        _showGuestFavouriteDialog.value = true
    }

    fun dismissGuestFavouriteDialog() {
        _showGuestFavouriteDialog.value = false
    }

    fun retry() {
        _error.value = null
        getRandomMeal()
        getRecipes()
        getCategories()
    }

    fun logout() {
        auth.signOut()
        _isLoggedOut.value = true
    }
}