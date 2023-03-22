package com.example.domain.converter

interface DataConverter {

    fun toJson(value: Any): String

    fun <T> fromJson(value: String, clazz: Class<T>): T
}

inline fun <reified T> DataConverter.fromJson(value: String) = fromJson(value, T::class.java)