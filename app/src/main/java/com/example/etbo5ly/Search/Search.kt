package com.example.etbo5ly.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.MealXXX
import com.example.etbo5ly.data.dto.searchX
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class Search(): ViewModel() {
    private val _SearchQ = MutableStateFlow("")
    val searchQ = _SearchQ.asStateFlow()
    private val _Result= MutableStateFlow<searchX?>(null)
    val result = _Result.asStateFlow()
    private val apiInstance = ApiClient.service
    private val datasource = RemoteDataSource(apiInstance)
    fun onSearchQueryChange(query: String){
        _SearchQ.value = query
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