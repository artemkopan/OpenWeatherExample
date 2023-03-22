package com.example.presentation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.converter.DataConverter
import com.example.domain.dispatcher.DispatchersProvider
import com.example.domain.error.ErrorHandler
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import com.example.domain.repo.ForecastRepo
import com.example.presentation.navigation.NavigationAction
import com.example.presentation.navigation.Router
import com.example.presentation.ui.details.DetailsNavParams
import com.example.presentation.ui.details.DetailsScreenNav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadForecast()
    }

    fun consumeAction(action: Action) {
        when (action) {
            Action.ConsumeErrorMessage -> _state.update { it.copy(errorMessage = null) }
            Action.Refresh -> loadForecast()
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

    private fun loadForecast() {
        _state.update { it.copy(loading = true, errorMessage = null) }
        viewModelScope.launch(
            dispatchers.io + CoroutineExceptionHandler { _, throwable -> showError(throwable) }
        ) {
            //fixme units - hardcoded for now. Can be provided from settings.
            //fixme latLon - hardcoed for now. Can be provided from location api.
            val forecast = repo.getForecast(
                position = LatLng(lat = 47.606209, lon = -122.332069),
                units = "metric"
            )
            _state.update {
                it.copy(
                    loading = false,
                    errorMessage = null,
                    forecast = forecast
                )
            }
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
