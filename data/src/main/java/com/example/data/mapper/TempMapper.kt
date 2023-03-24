package com.example.data.mapper

import com.example.data.database.entity.TempEmbedded
import com.example.data.network.response.TempResponse
import com.example.domain.model.Temp
import javax.inject.Inject

class TempMapper @Inject constructor() {

    fun toEntity(response: TempResponse): TempEmbedded = with(response) {
        return TempEmbedded(
            day = day ?: 0.0,
            night = night ?: 0.0,
            min = min ?: 0.0,
            max = max ?: 0.0
        )
    }

    fun toModel(entity: TempEmbedded): Temp = with(entity) {
        return Temp(
            day = day,
            night = night,
            min = min,
            max = max
        )
    }
}