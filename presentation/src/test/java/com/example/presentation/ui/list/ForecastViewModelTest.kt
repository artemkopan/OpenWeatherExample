@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.presentation.ui.list

import com.example.domain.converter.DataConverter
import com.example.domain.dispatcher.DispatchersProvider
import com.example.domain.error.ErrorHandler
import com.example.domain.model.Forecast
import com.example.domain.model.LatLng
import com.example.domain.model.Temperature
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

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

    @Test
    fun getForecast_returnSuccess_updateForecastInState() {
        //Arrange
        val forecast = mockk<Forecast>()

        coEvery { repoMock.getForecast(any(), any()) } returns forecast

        val target = createTarget()
        val initialState = target.state.value

        //Act
        dispatcher.scheduler.runCurrent()

        //Assert
        assertEquals(State(loading = true), initialState)
        assertEquals(
            State(loading = false, errorMessage = null, forecast = forecast),
            target.state.value
        )
        coVerify {
            repoMock.getForecast(
                position = LatLng(lat = 47.606209, lon = -122.332069),
                units = "metric"
            )
        }
    }

    @Test
    fun getForecast_throwsException_updateErrorInState() {
        //Arrange
        val message = "Some error"
        val exception = IOException(message)

        coEvery { repoMock.getForecast(any(), any()) } throws exception
        every { errorHandlerMock.formatDisplayError(exception) } returns message

        val target = createTarget()
        val initialState = target.state.value

        //Act
        dispatcher.scheduler.runCurrent()

        //Assert
        assertEquals(State(loading = true), initialState)
        assertEquals(
            State(loading = false, errorMessage = message, forecast = null),
            target.state.value
        )
    }

    @Test
    fun consumeErrorAction_clearError_updateStateWithoutError() {
        //Arrange
        val message = "Some error"
        val exception = IOException(message)

        coEvery { repoMock.getForecast(any(), any()) } throws exception
        every { errorHandlerMock.formatDisplayError(exception) } returns message

        val target = createTarget()

        //Act
        dispatcher.scheduler.runCurrent()
        target.consumeAction(Action.ConsumeErrorMessage)

        //Assert
        assertEquals(
            State(loading = false, errorMessage = null, forecast = null),
            target.state.value
        )
    }

    @Test
    fun consumeRefreshAction_loadForecast_verifyRepoWasCalled() {
        //Arrange
        coEvery { repoMock.getForecast(any(), any()) } returns mockk()

        val target = createTarget()

        //Act
        target.consumeAction(Action.Refresh)
        dispatcher.scheduler.runCurrent()

        //Assert
        coVerify(exactly = 2) { repoMock.getForecast(any(), any()) }
    }

    @Test
    fun consumeOpenDetailsAction_loadForecast_verifyRepoWasCalled() {
        //Arrange
        val temp = Temperature(0.1, 2.3, 3.4, 5.4)
        val weather = Weather("1", "desc", "main", "icon")
        val item = Forecast.Item(mockk(), temp, listOf(weather))
        val navParamsSlot = slot<DetailsNavParams>()

        coEvery { converterMock.toJson(capture(navParamsSlot)) } returns "model"
        coEvery { repoMock.getForecast(any(), any()) } returns mockk()
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
}