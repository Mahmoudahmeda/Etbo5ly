package com.example.etbo5ly.data.repository

import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.local.MealDao
import com.example.etbo5ly.data.local.entities.CalendarEntity
import com.example.etbo5ly.data.local.entities.FavoriteEntity
import com.example.etbo5ly.data.network.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MealRepository(
    private val remoteDataSource: IRemoteDataSource,
) : IMealRepository {
    override suspend fun getAMeal(): Response<MealResponse> {
        return remoteDataSource.getAMeal()
    }
}