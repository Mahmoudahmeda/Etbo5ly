package com.example.etbo5ly.dashboard_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.Meal
import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.remote.RetrofitInstance
import com.example.etbo5ly.data.repository.IMealRepository
import com.example.etbo5ly.ui.categories.Category
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

    private val _categories =  MutableStateFlow<List<Category>>(emptyList())
    val category = _categories

    private val _selectedItem = MutableStateFlow("")
    val selectedItem = _selectedItem

    init {
        // Load data when ViewModel is created
        getRandomMeal()
        getRecipes()
        getCategories()
        selectItem("Home")
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

    fun getCategories(){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCategories()
                _categories.value = response.categories.map {
                    Category(
                        name = it.strCategory,
                        image = it.strCategoryThumb
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectItem(item: String){
        _selectedItem.value = item
    }

    fun onFavoriteClick(mealId: String) {
        println("Favorite clicked for meal: id-$mealId")
    }
}