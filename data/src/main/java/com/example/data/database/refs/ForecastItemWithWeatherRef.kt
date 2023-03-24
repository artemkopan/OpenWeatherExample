package com.example.data.database.refs

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.data.database.entity.ForecastItemEntity
import com.example.data.database.entity.ForecastWeatherCrossEntity
import com.example.data.database.entity.WeatherEntity

data class ForecastItemWithWeatherRef(
    @Embedded
    val item: ForecastItemEntity,
    @Relation(
        parentColumn = "forecastId",
        entityColumn = "weatherId",
        associateBy = Junction(ForecastWeatherCrossEntity::class)
    )
    val weatherList: List<WeatherEntity>
)