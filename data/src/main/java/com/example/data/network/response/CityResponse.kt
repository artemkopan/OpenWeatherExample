package com.example.data.network.response

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null
)