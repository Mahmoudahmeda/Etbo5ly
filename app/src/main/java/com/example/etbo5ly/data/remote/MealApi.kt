package com.example.etbo5ly.data.remote

import retrofit2.http.GET

interface MealApi {

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse
}