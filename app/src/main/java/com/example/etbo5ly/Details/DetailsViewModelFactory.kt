package com.example.etbo5ly.Details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.etbo5ly.data.repository.CalendarRepository

class DetailsViewModelFactory(
    private val application: Application,
    private val repo: CalendarRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(detailsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return detailsScreenViewModel(application = application, repository = repo) as T
        }
        throw IllegalArgumentException("Cannot build: ${modelClass.name}")
    }
}