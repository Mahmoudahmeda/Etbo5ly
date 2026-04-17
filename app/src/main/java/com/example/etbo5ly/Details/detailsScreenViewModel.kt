package com.example.etbo5ly.Details

import android.R
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etbo5ly.data.dto.MealDetails
import com.example.etbo5ly.data.network.ApiClient
import com.example.etbo5ly.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class detailsScreenViewModel: ViewModel() {

    private val api = ApiClient.service
    private val datastore = RemoteDataSource(api)
    private val _Meal = MutableStateFlow<MealDetails?>(null)
    val meal = _Meal
    private val _isVideoEmbeddable = MutableStateFlow(false)
    val isVideoEmbeddable = _isVideoEmbeddable
    fun getMeal(id: String?){
        viewModelScope.launch {
            val response = datastore.getMealDetails(id)
            if (response.isSuccessful){
                _Meal.value = response.body()
            }
        }
    }
    fun setVideoLoadingError() {
        _isVideoEmbeddable.value = false
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