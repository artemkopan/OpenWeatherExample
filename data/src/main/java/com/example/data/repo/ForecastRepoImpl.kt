package com.example.data.repo

import com.example.data.database.dao.ForecastDao
import com.example.data.mapper.ForecastMapper
import com.example.data.network.client.ForecastRestClient
import com.example.domain.model.DataState
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import com.example.domain.repo.ForecastRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class ForecastRepoImpl @Inject constructor(
    private val dao: ForecastDao,
    private val restClient: ForecastRestClient,
    private val mapper: ForecastMapper
) : ForecastRepo {

    /**
     * Flow emits stream of data from database.
     * Local data will be updated after first subscription and will be emitted to flow.
     *
     * If we have local data:
     * 1. Emits local data.
     * 2. Make api call and then update local data.
     * 3. Database emits new local data after updating.
     *
     * If we do not have local data:
     * 1. Make api call and then update local data.
     * 2. Database emits new local data after updating.
     */
    override fun subscribeForecast(
        position: LatLng,
        units: String
    ): Flow<DataState<Forecast>> {
        return combine(
            //fixme db not supported different units. just loaded the latest network result
            subscribeDatabase(position),
            subscribeNetwork(position, units),
            ::DataState
        )
    }

    private fun subscribeDatabase(position: LatLng): Flow<DataState.State<Forecast>> {
        return flow {
            emit(DataState.State(loading = true))
            dao.subscribeForecast(position.lat, position.lon).mapNotNull { it.firstOrNull() }
                .map(mapper::toModel)
                .map { DataState.State(result = it) }
                .let { emitAll(it) }
        }.catch { throwable ->
            emit(DataState.State(throwable = throwable))
        }
    }

    private fun subscribeNetwork(position: LatLng, units: String): Flow<DataState.State<Forecast>> {
        return flow<DataState.State<Forecast>> {
            emit(DataState.State(loading = true))
            val response = restClient.getForecast(
                lat = position.lat,
                lon = position.lon,
                units = units
            )
            dao.upsertForecast(mapper.toEntity(response))
            // do not emit data to flow. It will be triggered by database.
        }.catch { throwable ->
            emit(DataState.State(throwable = throwable))
        }
    }
}