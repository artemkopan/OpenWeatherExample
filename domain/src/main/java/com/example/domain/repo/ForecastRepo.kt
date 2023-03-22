package com.example.domain.repo

import com.example.domain.model.Forecast
import com.example.domain.model.LatLng

interface ForecastRepo {

    suspend fun getForecast(position: LatLng, units: String): Forecast
}