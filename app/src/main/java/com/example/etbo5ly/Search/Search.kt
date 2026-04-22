package com.example.etbo5ly.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.Area
import com.example.etbo5ly.data.dto.CategoryDto
import com.example.etbo5ly.data.dto.Ingredient
import com.example.etbo5ly.data.dto.searchX
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Search(): ViewModel() {
    private val _SearchQ = MutableStateFlow("")
    val searchQ = _SearchQ.asStateFlow()
    
    private val _Result = MutableStateFlow<searchX?>(null)
    val result = _Result.asStateFlow()

    private val _Categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories = _Categories.asStateFlow()

    private val _Areas = MutableStateFlow<List<Area>>(emptyList())
    val areas = _Areas.asStateFlow()

    private val _Ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients = _Ingredients.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val apiInstance = ApiClient.service
    private val datasource = RemoteDataSource(apiInstance)

    fun onSearchQueryChange(query: String){
        _SearchQ.value = query
    }

    fun fetchCategories() {
        if (_Categories.value.isNotEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = datasource.getCategories()
                if (response.isSuccessful) {
                    _Categories.value = response.body()?.categories ?: emptyList()
                } else {
                    _error.value = "Failed to load categories"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchAreas() {
        if (_Areas.value.isNotEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = datasource.getAreas()
                if (response.isSuccessful) {
                    _Areas.value = response.body()?.areaList ?: emptyList()
                } else {
                    _error.value = "Failed to load countries"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchIngredients() {
        if (_Ingredients.value.isNotEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = datasource.getIngredients()
                if (response.isSuccessful) {
                    _Ingredients.value = response.body()?.ingredientList ?: emptyList()
                } else {
                    _error.value = "Failed to load ingredients"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun search(filter: String, query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = when (filter) {
                    "category" -> datasource.FilterByCategory(query)
                    "country" -> datasource.FilterByCountry(query)
                    "ingredient" -> datasource.FilterByIngredient(query)
                    else -> datasource.search(query)
                }

                if (response.isSuccessful) {
                    val body = response.body()
                    _Result.value = body
                    if (body?.meals.isNullOrEmpty()) {
                        _error.value = "No results found for '$query'"
                    }
                } else {
                    _error.value = "Network error"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
