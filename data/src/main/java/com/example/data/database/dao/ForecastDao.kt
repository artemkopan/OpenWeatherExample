package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.data.database.entity.CityEntity
import com.example.data.database.entity.ForecastItemEntity
import com.example.data.database.entity.ForecastWeatherCrossEntity
import com.example.data.database.entity.WeatherEntity
import com.example.data.database.refs.ForecastWithCityRef
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ForecastDao {

    @Transaction
    @Query(
        "select * from city where city.pos_lat = :lat and city.pos_lon = :lon limit 1"
    )
    abstract fun subscribeForecast(lat: Double, lon: Double): Flow<List<ForecastWithCityRef>>

    @Upsert
    protected abstract fun upsertCity(item: CityEntity)

    @Upsert
    protected abstract fun upsertForecastItem(item: ForecastItemEntity)

    @Upsert
    protected abstract fun upsertWeatherItem(item: WeatherEntity)

    @Upsert
    protected abstract fun upsertForecastWeatherItems(items: List<ForecastWeatherCrossEntity>)

    @Transaction
    open fun upsertForecast(item: ForecastWithCityRef) {
        // update city
        upsertCity(item.city)
        // create list for forecast-weather many-to-many relationship
        val forecastWeatherEntities = mutableListOf<ForecastWeatherCrossEntity>()

        item.forecastWithWeather.forEach {
            // upsert forecast
            upsertForecastItem(it.item)
            it.weatherList.forEach { weatherEntity ->
                // upsert weather
                upsertWeatherItem(weatherEntity)
                // create chain forecastId - weatherId
                forecastWeatherEntities.add(
                    ForecastWeatherCrossEntity(
                        forecastId = it.item.forecastId,
                        weatherId = weatherEntity.weatherId
                    )
                )
            }
        }
        upsertForecastWeatherItems(forecastWeatherEntities)
    }
}