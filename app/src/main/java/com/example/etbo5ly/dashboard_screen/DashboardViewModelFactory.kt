package com.example.etbo5ly.dashboard_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.etbo5ly.data.repository.CalendarRepository
import com.example.etbo5ly.data.repository.IMealRepository

class DashboardViewModelFactory(
    private val repository: IMealRepository,
    private val calendarepo: CalendarRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(repository, calendarepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}