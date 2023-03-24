package com.example.data.network.client

import com.example.data.network.response.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastRestClient {

    @GET("data/2.5/forecast/daily")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ): ForecastResponse
}