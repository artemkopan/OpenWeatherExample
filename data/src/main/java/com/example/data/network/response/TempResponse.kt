package com.example.data.network.response

import com.google.gson.annotations.SerializedName

data class TempResponse(
    @SerializedName("day")
    val day: Double? = null,
    @SerializedName("eve")
    val eve: Double? = null,
    @SerializedName("max")
    val max: Double? = null,
    @SerializedName("min")
    val min: Double? = null,
    @SerializedName("morn")
    val morn: Double? = null,
    @SerializedName("night")
    val night: Double? = null
)