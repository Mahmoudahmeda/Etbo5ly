package com.example.etbo5ly.data.repository

import com.example.etbo5ly.data.local.MealDao
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class CalendarRepository(
    private val mealDao: MealDao
): CalendarRepo {
    override suspend fun getMealsForWeek(userId: String, start: Long, end: Long): Flow<List<CalendarEntity>> {
        return mealDao.getMealsForWeek(userId, start, end)
    }

    override suspend fun insertMealToCalendar(meal: CalendarEntity) {
        mealDao.updateToCalendar(meal)
    }

    override suspend fun deleteMealFromCalendar(meal: CalendarEntity) {
        mealDao.deleteFromCalendar(meal)
    }

    override fun getFavorites(userId: String): Flow<List<FavoriteEntity>> {
        return mealDao.getAllFavorites(userId)
    }

    override suspend fun insertFavorite(favorite: FavoriteEntity) {
        mealDao.addFavorite(favorite)
    }

    override suspend fun deleteFavorite(userId: String, recipeId: String) {
        mealDao.removeFavorite(userId, recipeId)
    }
}