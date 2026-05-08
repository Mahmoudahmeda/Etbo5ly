package com.example.etbo5ly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val recipeId: String,
    val recipeName: String,
    val recipeImage: String,
    val recipeInstructions: String,
    val recipeIngredients: String,
    val recipeTags: String,
    val recipeCategory: String,
    val recipeArea: String,
    val recipeYoutube: String
)
