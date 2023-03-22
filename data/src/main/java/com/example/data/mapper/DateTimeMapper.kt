package com.example.data.mapper

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Duration

class DateTimeMapper @Inject constructor() {

    fun toLocalDate(value: Duration): LocalDate {
        val instant = Instant.ofEpochMilli(value.inWholeMilliseconds)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
    }
}