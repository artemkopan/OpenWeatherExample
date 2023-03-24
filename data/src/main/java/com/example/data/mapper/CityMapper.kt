package com.example.data.mapper

import com.example.data.database.entity.CityEntity
import com.example.data.network.response.CityResponse
import com.example.domain.model.City
import javax.inject.Inject

class CityMapper @Inject constructor(
    private val latLngMapper: LatLngMapper
) {

    fun toModel(entity: CityEntity): City = with(entity) {
        return City(
            id = id,
            country = country,
            name = name
        )
    }

    fun toEntity(response: CityResponse): CityEntity = with(response) {
        return CityEntity(
            id = id.orEmpty(),
            country = country.orEmpty(),
            name = name.orEmpty(),
            latLng = latLngMapper.toEntity(requireNotNull(response.coord))
        )
    }
}