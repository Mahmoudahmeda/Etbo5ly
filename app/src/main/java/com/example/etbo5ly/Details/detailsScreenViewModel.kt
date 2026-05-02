package com.example.etbo5ly.Details

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class detailsScreenViewModel : ViewModel() {

    private val api = ApiClient.service
    private val datastore = RemoteDataSource(api)

    private val _Meal = MutableStateFlow<MealDetails?>(null)
    val meal = _Meal


    fun getMeal(id: String?) {
        viewModelScope.launch {
            val response = datastore.getMealDetails(id)
            if (response.isSuccessful) {
                _Meal.value = response.body()
            }
        }
    }


    fun getVideoId(url: String?): String {
        return url?.substringAfter("v=", "")?.substringBefore("&") ?: ""
    }

    fun getYoutubeIntent(url: String?): Intent? {
        return if (!url.isNullOrBlank()) {
            Intent(Intent.ACTION_VIEW, url.toUri())
        } else null
    }
}