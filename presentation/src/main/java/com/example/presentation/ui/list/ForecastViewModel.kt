package com.example.presentation.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.converter.DataConverter
import com.example.domain.dispatcher.DispatchersProvider
import com.example.domain.error.ErrorHandler
import com.example.domain.model.DataState
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import com.example.domain.repo.ForecastRepo
import com.example.presentation.navigation.NavigationAction
import com.example.presentation.navigation.Router
import com.example.presentation.ui.details.DetailsNavParams
import com.example.presentation.ui.details.DetailsScreenNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val repo: ForecastRepo,
    private val errorHandler: ErrorHandler,
    private val router: Router,
    private val converter: DataConverter,
    private val dispatchers: DispatchersProvider
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private var subscriptionJob: Job? = null

    init {
        subscribeForecast()
    }

    fun consumeAction(action: Action) {
        when (action) {
            Action.ConsumeErrorMessage -> _state.update { it.copy(errorMessage = null) }
            Action.Refresh -> subscribeForecast()
            is Action.ShowDetails -> showDetails(action.item)
        }
    }

    private fun showDetails(item: Forecast.Item) {
        viewModelScope.launch(dispatchers.main) {
            router.route(
                NavigationAction.Navigate(
                    DetailsScreenNav.build(
                        converter = converter,
                        params = DetailsNavParams(
                            minTemp = item.temp.min,
                            maxTemp = item.temp.max,
                            dayTemp = item.temp.day,
                            nightTemp = item.temp.night,
                            icon = item.weather.firstOrNull()?.icon.orEmpty()
                        )
                    )
                )
            )
        }
    }

    private fun subscribeForecast() {
        _state.update { it.copy(loading = true, errorMessage = null) }
        subscriptionJob?.cancel()
        subscriptionJob = repo.subscribeForecast(
            position = LatLng(lat = 47.6062, lon = -122.332),
            units = "metric"
        )
            .flowOn(dispatchers.io)
            .onEach { dataStates ->
                Log.d("STATE", dataStates.toString())
                _state.update {
                    it.copy(
                        loading = dataStates.remote.loading,
                        errorMessage = dataStates.errorMessage(),
                        forecast = dataStates.local.result
                    )
                }
            }
            .catch { showError(it) }
            .run { viewModelScope.launch(dispatchers.io) { collect() } }
    }

    private fun DataState<*>.errorMessage(): String? {
        // combine errors message from states
        if (local.throwable == null && remote.throwable == null) return null
        return buildString {
            local.throwable?.let { append(errorHandler.formatDisplayError(it)) }
            if (isNotBlank() && remote.throwable != null) append('\n')
            remote.throwable?.let { append(errorHandler.formatDisplayError(it)) }
        }
    }

    private fun showError(throwable: Throwable) {
        _state.update {
            it.copy(
                loading = false,
                errorMessage = errorHandler.formatDisplayError(throwable)
            )
        }
    }

    data class State(
        val loading: Boolean = false,
        val errorMessage: String? = null,
        val forecast: Forecast? = null
    ) {
        fun showError() = errorMessage != null
        fun errorMessage() = requireNotNull(errorMessage)

        fun showForecast() = forecast != null
        fun forecast() = requireNotNull(forecast)
    }

    sealed interface Action {
        object Refresh : Action
        object ConsumeErrorMessage : Action
        data class ShowDetails(val item: Forecast.Item) : Action
    }
}
