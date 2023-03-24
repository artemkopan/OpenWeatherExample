package com.example.domain.model

import java.time.LocalDate

data class Forecast(
    val city: City,
    val items: List<Item>
) {
    data class Item(
        val dt: LocalDate,
        val temp: Temp,
        val weather: List<Weather>
    )
}


