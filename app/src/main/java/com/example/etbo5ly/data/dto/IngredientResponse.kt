package com.example.etbo5ly.data.dto

import com.google.gson.annotations.SerializedName

data class IngredientResponse(
    @SerializedName("meals")
    val ingredientList: List<Ingredient>
)