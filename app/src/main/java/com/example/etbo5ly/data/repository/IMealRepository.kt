package com.example.etbo5ly.data.repository

import com.example.etbo5ly.data.dto.MealResponse
import retrofit2.Response

interface IMealRepository {
    suspend fun getAMeal() : Response<MealResponse>
}