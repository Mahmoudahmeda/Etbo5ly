package com.example.etbo5ly.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Upsert
    suspend fun updateToCalendar(calendarMeal: CalendarEntity)

    @Delete
    suspend fun deleteFromCalendar(calendarMeal: CalendarEntity)

    @Query("SELECT * FROM CalendarEntity WHERE userId = :userId AND date BETWEEN :start AND :end ORDER BY date ASC")
    fun getMealsForWeek(userId: String, start: Long, end: Long): Flow<List<CalendarEntity>>

    @Upsert
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE userId = :userId AND recipeId = :recipeId")
    suspend fun removeFavorite(userId: String, recipeId: String)

    @Query("SELECT * FROM FavoriteEntity WHERE userId = :userId")
    fun getAllFavorites(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteEntity WHERE userId = :userId AND recipeId = :recipeId)")
    fun isFavorite(userId: String, recipeId: String): Flow<Boolean>
}