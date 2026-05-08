package com.example.etbo5ly.data.repository

import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface CalendarRepo {
    suspend fun getMealsForWeek(userId: String, start: Long, end: Long): Flow<List<CalendarEntity>>
    suspend fun insertMealToCalendar(meal: CalendarEntity)
    suspend fun deleteMealFromCalendar(meal: CalendarEntity)

    fun getFavorites(userId: String): Flow<List<FavoriteEntity>>
    suspend fun insertFavorite(favorite: FavoriteEntity)
    suspend fun deleteFavorite(userId: String, recipeId: String)
}