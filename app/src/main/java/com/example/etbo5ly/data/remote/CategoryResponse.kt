package com.example.etbo5ly.data.remote

data class CategoryResponse(
    val categories: List<CategoryDto>
)

data class CategoryDto(
    val strCategory: String,
    val strCategoryThumb: String
)