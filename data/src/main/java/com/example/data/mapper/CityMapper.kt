package com.example.data.mapper

import com.example.data.network.response.CityResponse
import com.example.domain.model.City
import javax.inject.Inject

class CityMapper @Inject constructor() {

    fun toModel(response: CityResponse): City = with(response) {
        return City(
            id = id.orEmpty(),
            country = country.orEmpty(),
            name = name.orEmpty()
        )
    }
}