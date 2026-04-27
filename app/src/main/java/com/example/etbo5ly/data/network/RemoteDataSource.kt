package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.AreaListResponse
import com.example.etbo5ly.data.dto.CategoryResponse
import com.example.etbo5ly.data.dto.IngredientResponse
import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.dto.searchX
import retrofit2.Response

class RemoteDataSource(
    private val apiClient: ApiService
) : IRemoteDataSource{
    override suspend fun getAMeal(): Response<MealResponse> {
        return apiClient.getAMeal()
    }

    override suspend fun getMealDetails(i: String?): Response<MealDetails> {
        return apiClient.getMealDetails(i)
    }

    override suspend fun getCategories(): Response<CategoryResponse> {
        return apiClient.getCategories()
    }

    override suspend fun getAreas(): Response<AreaListResponse> {
        return apiClient.getAreas()
    }

    override suspend fun getIngredients(): Response<IngredientResponse> {
        return apiClient.getIngredients()
    }

    override suspend fun search(query: String): Response<searchX> {
        return apiClient.Search(query)
    }

    override suspend fun FilterByCategory(query: String): Response<searchX> {
        return apiClient.SearchByCategory(query)
    }

    override suspend fun FilterByCountry(query: String): Response<searchX> {
        return apiClient.SearchByCountry(query)
    }

    override suspend fun FilterByIngredient(query: String): Response<searchX> {
        return apiClient.SearchByIngredient(query)
    }
}
