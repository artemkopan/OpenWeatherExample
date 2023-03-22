package com.example.data.network.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("main")
    val main: String? = null
)