package com.example.presentation.main

import androidx.lifecycle.ViewModel
import com.example.presentation.ui.list.ForecastScreenNav
import com.example.presentation.navigation.NavigationScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WeatherAppViewModel @Inject constructor(
    navigationScreens: Set<@JvmSuppressWildcards NavigationScreen>
) : ViewModel() {

    private val _state = MutableStateFlow(
        State(startDestination = ForecastScreenNav.NAME, screens = navigationScreens)
    )
    val state = _state.asStateFlow()

    data class State(
        val startDestination: String,
        val screens: Set<@JvmSuppressWildcards NavigationScreen>
    )
}