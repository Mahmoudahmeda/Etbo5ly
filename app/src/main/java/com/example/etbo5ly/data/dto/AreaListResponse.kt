package com.example.etbo5ly.data.dto

import com.google.gson.annotations.SerializedName

data class AreaListResponse(
    @SerializedName("meals")
    val areaList: List<Area>
)
