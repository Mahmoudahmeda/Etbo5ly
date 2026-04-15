package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.dto.searchX
import retrofit2.Response

interface IRemoteDataSource {
    suspend fun getAMeal(): Response<MealResponse>
   
    suspend fun getMealDetails(i: String?): Response<MealDetails>

    suspend fun search(query: String): Response<searchX>

    suspend fun FilterByCategory(query: String): Response<searchX>

    suspend fun FilterByCountry(query: String): Response<searchX>

    suspend fun FilterByIngredient(query: String): Response<searchX>

}