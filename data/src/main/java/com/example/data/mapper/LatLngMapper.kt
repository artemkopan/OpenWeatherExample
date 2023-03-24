package com.example.data.mapper

import com.example.data.database.entity.LatLngEmbedded
import com.example.data.network.response.LatLngResponse
import javax.inject.Inject

class LatLngMapper @Inject constructor() {

    fun toEntity(response: LatLngResponse): LatLngEmbedded {
        return LatLngEmbedded(
            lat = response.lat ?: 0.0,
            lon = response.lon ?: 0.0
        )
    }
}