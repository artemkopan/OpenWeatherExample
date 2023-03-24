@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.presentation.ui.list

import com.example.domain.converter.DataConverter
import com.example.domain.dispatcher.DispatchersProvider
import com.example.domain.error.ErrorHandler
import com.example.domain.model.DataState
import com.example.domain.model.DataState.*
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import com.example.domain.model.Temp
import com.example.domain.model.Weather
import com.example.domain.repo.ForecastRepo
import com.example.presentation.navigation.Router
import com.example.presentation.ui.details.DetailsNavParams
import com.example.presentation.ui.list.ForecastViewModel.Action
import com.example.presentation.ui.list.ForecastViewModel.State
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.IOException
import java.io.PrintStream
import java.util.stream.Stream

class ForecastViewModelTest {

    private val repoMock = mockk<ForecastRepo>(relaxed = true)
    private val errorHandlerMock = mockk<ErrorHandler>(relaxed = true)
    private val routerMock = mockk<Router>(relaxed = true)
    private val converterMock = mockk<DataConverter>(relaxed = true)
    private val dispatchersMock = mockk<DispatchersProvider>(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        every { dispatchersMock.io } returns dispatcher
        every { dispatchersMock.main } returns dispatcher
    }

    @ParameterizedTest
    @MethodSource("provideTestStates")
    fun subscribeForecast(
        dataState: DataState<Forecast>,
        expectedState: State
    ) {
        //Arrange
        every { dispatchersMock.io } returns Dispatchers.Unconfined
        every { dispatchersMock.main } returns Dispatchers.Unconfined
        coEvery { repoMock.subscribeForecast(any(), any()) } returns  flowOf(dataState)
        every { errorHandlerMock.formatDisplayError(any()) } answers {
            (it.invocation.args.first() as Throwable).message.orEmpty()
        }

        //Act
        val target = createTarget()
        val initialState = target.state.value

        //Assert
        assertEquals(expectedState, initialState)
        coVerify {
            repoMock.subscribeForecast(
                position = LatLng(lat = 47.6062, lon = -122.332),
                units = "metric"
            )
        }
    }

    @Test
    fun consumeRefreshAction_loadForecast_verifyRepoWasCalled() {
        //Arrange
        coEvery { repoMock.subscribeForecast(any(), any()) } returns mockk()

        val target = createTarget()

        //Act
        target.consumeAction(Action.Refresh)
        dispatcher.scheduler.runCurrent()

        //Assert
        coVerify(exactly = 2) { repoMock.subscribeForecast(any(), any()) }
    }

    @Test
    fun consumeOpenDetailsAction_loadForecast_verifyRepoWasCalled() {
        //Arrange
        val temp = Temp(0.1, 2.3, 3.4, 5.4)
        val weather = Weather("1", "desc", "main", "icon")
        val item = Forecast.Item(mockk(), temp, listOf(weather))
        val navParamsSlot = slot<DetailsNavParams>()

        coEvery { converterMock.toJson(capture(navParamsSlot)) } returns "model"
        coEvery { repoMock.subscribeForecast(any(), any()) } returns mockk()
        coJustRun { routerMock.route(any()) }

        val target = createTarget()

        //Act
        target.consumeAction(Action.ShowDetails(item))
        dispatcher.scheduler.runCurrent()

        //Assert
        with(navParamsSlot.captured) {
            assertEquals(temp.min, minTemp)
            assertEquals(temp.max, maxTemp)
            assertEquals(temp.day, dayTemp)
            assertEquals(temp.night, nightTemp)
            assertEquals(weather.icon, icon)
        }
    }

    private fun createTarget() = ForecastViewModel(
        repoMock,
        errorHandlerMock,
        routerMock,
        converterMock,
        dispatchersMock
    )

    companion object {

        @JvmStatic
        fun provideTestStates(): Stream<Arguments> {
            val forecastMock = mockk<Forecast>()
            return Stream.of(
                Arguments.of(
                    DataState<Forecast>(
                        local = DataState.State(loading = true),
                        remote = DataState.State()
                    ),
                    State(loading = false, errorMessage = null, forecast = null)
                ),
                Arguments.of(
                    DataState<Forecast>(
                        local = DataState.State(loading = false),
                        remote = DataState.State(loading = true)
                    ),
                    State(loading = true, errorMessage = null, forecast = null)
                ),
                Arguments.of(
                    DataState<Forecast>(
                        local = State(throwable = Throwable("error")),
                        remote = DataState.State()
                    ),
                    State(loading = false, errorMessage = "error", forecast = null)
                ),
                Arguments.of(
                    DataState<Forecast>(
                        local = DataState.State(),
                        remote = State(throwable = Throwable("error"))
                    ),
                    State(loading = false, errorMessage = "error", forecast = null)
                ),
                Arguments.of(
                    DataState<Forecast>(
                        local = State(throwable = Throwable("error1")),
                        remote = State(throwable = Throwable("error2"))
                    ),
                    State(loading = false, errorMessage = "error1\nerror2", forecast = null)
                ),
                Arguments.of(
                    DataState(
                        local = State(result = forecastMock),
                        remote = State(throwable = Throwable("error2"))
                    ),
                    State(loading = false, errorMessage = "error2", forecast = forecastMock)
                ),
                Arguments.of(
                    DataState(
                        local = State(result = forecastMock),
                        remote = DataState.State(loading = true)
                    ),
                    State(loading = true, errorMessage = null, forecast = forecastMock)
                )
            )
        }
    }
}