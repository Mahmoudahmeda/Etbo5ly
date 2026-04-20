package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.dto.MealResponse
import com.example.etbo5ly.data.dto.CategoryResponse
import com.example.etbo5ly.data.dto.AreaListResponse
import com.example.etbo5ly.data.dto.IngredientResponse
import com.example.etbo5ly.data.dto.searchX
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random.php")
    suspend fun getAMeal() : Response<MealResponse>

    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("list.php?a=list")
    suspend fun getAreas(): Response<AreaListResponse>

    @GET("list.php?i=list")
    suspend fun getIngredients(): Response<IngredientResponse>

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") i: String?) : Response<MealDetails>

    @GET("search.php")
    suspend fun Search(@Query("s") s: String): Response<searchX>

    @GET("filter.php")
    suspend fun SearchByCategory(@Query("c") c: String): Response<searchX>

    @GET("filter.php")
    suspend fun SearchByCountry(@Query("a") a: String): Response<searchX>

    @GET("filter.php")
    suspend fun SearchByIngredient(@Query("i") i: String): Response<searchX>
}
