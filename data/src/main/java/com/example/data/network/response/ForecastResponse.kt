package com.example.data.network.response

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("city")
    val city: CityResponse? = null,
    @SerializedName("cnt")
    val cnt: Int? = null,
    @SerializedName("list")
    val list: List<Item>? = null
) {
    data class Item(
        @SerializedName("clouds")
        val clouds: Int? = null,
        @SerializedName("deg")
        val deg: Int? = null,
        @SerializedName("gust")
        val gust: Double? = null,
        @SerializedName("humidity")
        val humidity: Int? = null,
        @SerializedName("pop")
        val pop: Double? = null,
        @SerializedName("pressure")
        val pressure: Int? = null,
        @SerializedName("speed")
        val speed: Double? = null,
        @SerializedName("temp")
        val temp: TempResponse? = null,
        @SerializedName("weather")
        val weather: List<WeatherResponse?>? = null,
        @SerializedName("dt")
        val dt: Long? = null,
    )
}