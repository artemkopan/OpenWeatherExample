package com.example.domain.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class DataConverterImpl @Inject constructor(private val gson: Gson) : DataConverter {

    override fun toJson(value: Any): String {
        return gson.toJson(value)
    }

    override fun <T> fromJson(value: String, clazz: Class<T>): T {
        return gson.fromJson<T>(value, TypeToken.get(clazz))
    }
}