package com.example.data.repo

import com.example.data.mapper.ForecastMapper
import com.example.data.network.client.ForecastRestClient
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import com.example.domain.repo.ForecastRepo
import javax.inject.Inject

class ForecastRepoImpl @Inject constructor(
    private val restClient: ForecastRestClient,
    private val mapper: ForecastMapper
) : ForecastRepo {

    override suspend fun getForecast(position: LatLng, units: String): Forecast {
        return restClient.getWeather(
            lat = position.lat,
            lon = position.lon,
            units = units
        ).let(mapper::toModel)
    }
}