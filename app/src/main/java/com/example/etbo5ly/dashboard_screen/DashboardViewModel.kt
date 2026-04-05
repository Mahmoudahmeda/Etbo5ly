package com.example.etbo5ly.dashboard_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.repository.IMealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class DashboardViewModel(
    private val repository : IMealRepository
) : ViewModel() {

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal : StateFlow<Meal?> = _meal

    private val _recipes = MutableStateFlow<List<Meal>>(emptyList())
    val recipes: StateFlow<List<Meal>> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Load data when ViewModel is created
        getRandomMeal()
        getRecipes()
    }

    fun getRandomMeal(){
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
                for (i in 1..count) {
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

    fun onFavoriteClick(mealId: String) {
        println("Favorite clicked for meal: id-$mealId")
    }
}