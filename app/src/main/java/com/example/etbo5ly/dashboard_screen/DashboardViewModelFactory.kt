package com.example.etbo5ly.dashboard_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.etbo5ly.data.repository.IMealRepository

class DashboardViewModelFactory(
    private val repository: IMealRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            try {
                return DashboardViewModel(repository) as T
            }catch (e: Exception){

            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}