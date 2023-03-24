package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.ForecastDao
import com.example.data.database.entity.CityEntity
import com.example.data.database.entity.ForecastItemEntity
import com.example.data.database.entity.WeatherEntity
import com.example.data.database.entity.ForecastWeatherCrossEntity

@Database(
    entities = [
        CityEntity::class,
        ForecastItemEntity::class,
        WeatherEntity::class,
        ForecastWeatherCrossEntity::class
    ],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}