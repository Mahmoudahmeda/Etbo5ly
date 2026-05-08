package com.example.etbo5ly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val mealId: String,
    val mealName: String,
    val mealImage: String,
    val date: Long,
    val workRequestId: String? = null
)
