package com.example.data.mapper

import com.example.data.database.entity.WeatherEntity
import com.example.data.network.env.EnvironmentProvider
import com.example.data.network.response.WeatherResponse
import com.example.domain.model.Weather
import javax.inject.Inject

class WeatherMapper @Inject constructor(
    private val environmentProvider: EnvironmentProvider
) {

    fun toEntity(response: WeatherResponse): WeatherEntity = with(response) {
        return WeatherEntity(
            weatherId = id.orEmpty(),
            description = description.orEmpty(),
            main = main.orEmpty(),
            icon = icon.orEmpty()
        )
    }

    fun toModel(entity: WeatherEntity): Weather = with(entity) {
        return Weather(
            id = weatherId,
            description = description,
            main = main,
            icon = environmentProvider.getIconUrl(icon)
        )
    }
}