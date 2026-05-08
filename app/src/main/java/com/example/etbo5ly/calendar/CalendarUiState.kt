package com.example.etbo5ly.calendar

import com.example.etbo5ly.data.local.entities.CalendarEntity

data class CalendarUiState(
    val selectedDate: Long = System.currentTimeMillis(),
    val selectedDayName: String = "",
    val selectedDateFormatted: String = "",
    val mealsForSelectedDay: List<CalendarEntity> = emptyList(),
    val mealsForWeek: List<CalendarEntity> = emptyList(),
)