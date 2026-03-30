package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET
    suspend fun getAMeal() : Response<MealResponse>
}