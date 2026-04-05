package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealResponse
import retrofit2.Response

class RemoteDataSource(
    private val apiClient: ApiService
) : IRemoteDataSource{
    override suspend fun getAMeal(): Response<MealResponse> {
        return apiClient.getAMeal()
    }
}