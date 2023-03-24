package com.example.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "name")
    val name: String,
    @Embedded(prefix = "pos")
    val latLng: LatLngEmbedded,
)