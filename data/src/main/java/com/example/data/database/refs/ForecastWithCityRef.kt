package com.example.data.database.refs

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.database.entity.CityEntity
import com.example.data.database.entity.ForecastItemEntity

data class ForecastWithCityRef(
    @Embedded
    val city: CityEntity,
    @Relation(
        entity = ForecastItemEntity::class,
        parentColumn = "id",
        entityColumn = "cityId"
    )
    val forecastWithWeather: List<ForecastItemWithWeatherRef>
)