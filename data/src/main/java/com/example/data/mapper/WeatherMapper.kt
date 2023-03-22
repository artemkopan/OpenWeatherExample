package com.example.data.mapper

import com.example.data.network.env.EnvironmentProvider
import com.example.data.network.response.WeatherResponse
import com.example.domain.model.Weather
import javax.inject.Inject

class WeatherMapper @Inject constructor(
    private val environmentProvider: EnvironmentProvider
) {

    fun toModel(response: WeatherResponse): Weather = with(response) {
        return Weather(
            id = id.orEmpty(),
            description = description.orEmpty(),
            main = main.orEmpty(),
            icon = environmentProvider.getIconUrl(icon.orEmpty())
        )
    }
}