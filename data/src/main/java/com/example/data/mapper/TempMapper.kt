package com.example.data.mapper

import com.example.data.network.response.TempResponse
import com.example.domain.model.Temperature
import javax.inject.Inject

class TempMapper @Inject constructor() {

    fun toModel(response: TempResponse): Temperature = with(response) {
        return Temperature(
            day = day ?: 0.0,
            night = night ?: 0.0,
            min = min ?: 0.0,
            max = max ?: 0.0
        )
    }
}