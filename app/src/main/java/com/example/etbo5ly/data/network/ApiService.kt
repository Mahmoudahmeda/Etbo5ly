package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.dto.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun getAMeal() : Response<MealResponse>
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse
    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") i: String?) : Response<MealDetails>
}