package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "forecast_weather",
    primaryKeys = ["forecastId", "weatherId"],
    foreignKeys = [
        ForeignKey(
            entity = ForecastItemEntity::class,
            parentColumns = ["forecastId"],
            childColumns = ["forecastId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WeatherEntity::class,
            parentColumns = ["weatherId"],
            childColumns = ["weatherId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ForecastWeatherCrossEntity(
    val forecastId: String,
    val weatherId: String,
)