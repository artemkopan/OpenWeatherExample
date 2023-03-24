package com.example.data.network.response

import com.google.gson.annotations.SerializedName

data class LatLngResponse(
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null
)