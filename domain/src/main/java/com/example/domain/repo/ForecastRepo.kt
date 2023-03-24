package com.example.domain.repo

import com.example.domain.model.DataState
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import kotlinx.coroutines.flow.Flow

interface ForecastRepo {

    fun subscribeForecast(position: LatLng, units: String): Flow<DataState<Forecast>>
}