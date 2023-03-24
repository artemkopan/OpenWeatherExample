package com.example.data.database.entity

import androidx.room.ColumnInfo

data class LatLngEmbedded(
    @ColumnInfo(name = "_lat")
    val lat: Double,
    @ColumnInfo(name = "_lon")
    val lon: Double
)