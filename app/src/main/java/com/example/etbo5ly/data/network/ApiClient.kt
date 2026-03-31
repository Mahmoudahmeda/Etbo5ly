package com.example.etbo5ly.data.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val gson = GsonBuilder().serializeNulls().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service by lazy { retrofit.create(ApiService::class.java) }
}