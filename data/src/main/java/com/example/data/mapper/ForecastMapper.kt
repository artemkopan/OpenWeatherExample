package com.example.data.mapper

import com.example.data.network.response.ForecastResponse
import com.example.domain.model.Forecast
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class ForecastMapper @Inject constructor(
    private val cityMapper: CityMapper,
    private val tempMapper: TempMapper,
    private val weatherMapper: WeatherMapper,
    private val dateTimeMapper: DateTimeMapper
) {

    fun toModel(response: ForecastResponse): Forecast = with(response) {
        return Forecast(
            city = cityMapper.toModel(requireNotNull(city)),
            items = list.orEmpty().map(::toModel)
        )
    }

    private fun toModel(response: ForecastResponse.Item): Forecast.Item = with(response) {
        Forecast.Item(
            dt = requireNotNull(dt).let { dateTimeMapper.toLocalDate(it.seconds) },
            temp = tempMapper.toModel(requireNotNull(temp)),
            weather = weather.orEmpty().mapNotNull { it?.let(weatherMapper::toModel) },
        )
    }
}