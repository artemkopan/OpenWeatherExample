package com.example.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @ColumnInfo(name = "weatherId")
    @PrimaryKey
    val weatherId: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "main")
    val main: String,
    @ColumnInfo(name = "icon")
    val icon: String
)