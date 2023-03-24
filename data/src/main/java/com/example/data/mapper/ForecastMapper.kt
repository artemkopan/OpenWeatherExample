package com.example.data.mapper

import com.example.data.database.entity.ForecastItemEntity
import com.example.data.database.refs.ForecastItemWithWeatherRef
import com.example.data.database.refs.ForecastWithCityRef
import com.example.data.network.response.ForecastResponse
import com.example.domain.model.City
import com.example.domain.model.Forecast
import com.example.domain.model.Weather
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class ForecastMapper @Inject constructor(
    private val cityMapper: CityMapper,
    private val tempMapper: TempMapper,
    private val weatherMapper: WeatherMapper,
    private val dateTimeMapper: DateTimeMapper
) {

    fun toEntity(response: ForecastResponse): ForecastWithCityRef = with(response) {
        val city = cityMapper.toEntity(requireNotNull(response.city))
        return ForecastWithCityRef(
            city = city,
            forecastWithWeather = list.orEmpty().map { forecastItem ->
                ForecastItemWithWeatherRef(
                    item = toEntity(city.id, forecastItem),
                    weatherList = forecastItem.weather.orEmpty().mapNotNull {
                        it?.let(weatherMapper::toEntity)
                    }
                )
            }
        )
    }

    fun toModel(entity: ForecastWithCityRef): Forecast {
        return Forecast(
            city = cityMapper.toModel(entity.city),
            items = entity.forecastWithWeather.map {
                toModel(entity = it.item, weather = it.weatherList.map(weatherMapper::toModel))
            }
        )
    }

    private fun toEntity(
        cityId: String,
        response: ForecastResponse.Item
    ): ForecastItemEntity = with(response) {
        ForecastItemEntity(
            dt = requireNotNull(dt),
            temp = tempMapper.toEntity(requireNotNull(temp)),
            forecastId = cityId + dt,
            cityId = cityId
        )
    }

    private fun toModel(
        entity: ForecastItemEntity,
        weather: List<Weather>
    ): Forecast.Item = with(entity) {
        Forecast.Item(
            dt = dt.let { dateTimeMapper.toLocalDate(it.seconds) },
            temp = tempMapper.toModel(temp),
            weather = weather,
        )
    }
}