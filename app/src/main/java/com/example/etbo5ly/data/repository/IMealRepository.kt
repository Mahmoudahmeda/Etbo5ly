package com.example.etbo5ly.data.repository

import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IMealRepository {
    suspend fun getAMeal() : Response<MealResponse>
}