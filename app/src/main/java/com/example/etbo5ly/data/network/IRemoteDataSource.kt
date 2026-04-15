package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealResponse
import retrofit2.Response

interface IRemoteDataSource {
    suspend fun getAMeal(): Response<MealResponse>
    suspend fun getMealDetails(i: String?): Response<MealDetails>
}