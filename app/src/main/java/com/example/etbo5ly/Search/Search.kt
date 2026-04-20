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

    private val apiInstance = ApiClient.service
    private val datasource = RemoteDataSource(apiInstance)

    fun onSearchQueryChange(query: String){
        _SearchQ.value = query
    }

    fun fetchCategories() {
        viewModelScope.launch {
            val response = datasource.getCategories()
            if (response.isSuccessful) {
                _Categories.value = response.body()?.categories ?: emptyList()
            }
        }
    }

    fun fetchAreas() {
        viewModelScope.launch {
            val response = datasource.getAreas()
            if (response.isSuccessful) {
                _Areas.value = response.body()?.areaList ?: emptyList()
            }
        }
    }

    fun fetchIngredients() {
        viewModelScope.launch {
            val response = datasource.getIngredients()
            if (response.isSuccessful) {
                _Ingredients.value = response.body()?.ingredientList ?: emptyList()
            }
        }
    }

    fun search(filter: String) {
        viewModelScope.launch {
            val query = _SearchQ.value
            val response = when (filter) {
                "Categories" -> datasource.FilterByCategory(query)
                "Countries" -> datasource.FilterByCountry(query)
                "Ingredients" -> datasource.FilterByIngredient(query)
                else -> datasource.search(query)
            }

            if (response.isSuccessful) {
                _Result.value = response.body()
            }
        }
    }
}
