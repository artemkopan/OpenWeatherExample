package com.example.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "forecast",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ForecastItemEntity(
    @PrimaryKey
    @ColumnInfo("forecastId")
    val forecastId: String,
    @ColumnInfo(name = "cityId")
    val cityId: String,
    @ColumnInfo("dt")
    val dt: Long,
    @Embedded(prefix = "temp")
    val temp: TempEmbedded,
)