package com.example.etbo5ly.data.dto

data class MealX(
    val idMeal: String,
    val strArea: String,
    val strCategory: String,
    val strInstructions: String,
    val strMeal: String,
    val strMealThumb: String,
    val strTags: String?,
    val strYoutube: String?,
    val ingredients: List<Pair<String, String>>
)