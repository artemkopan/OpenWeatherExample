package com.example.data.database.entity

import androidx.room.ColumnInfo

data class TempEmbedded(
    @ColumnInfo(name = "_day")
    val day: Double,
    @ColumnInfo(name = "_night")
    val night: Double,
    @ColumnInfo(name = "_min")
    val min: Double,
    @ColumnInfo(name = "_max")
    val max: Double
)
