package com.example.presentation.ui.list

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.example.presentation.navigation.NavigationScreen
import javax.inject.Inject

class ForecastScreenNav @Inject constructor() : NavigationScreen {
    override val name: String = NAME
    override val content: @Composable (NavBackStackEntry) -> Unit = { ForecastScreen() }

    companion object {
        const val NAME = "FeedList"
    }
}