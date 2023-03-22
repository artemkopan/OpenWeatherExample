package com.example.presentation.navigation

import androidx.navigation.NavArgument
import androidx.navigation.NavOptions

sealed interface NavigationAction {

    data class Navigate(
        val screen: String,
        val options: NavOptions? = null,
        val args: List<NavArgument> = emptyList()
    ) : NavigationAction

    object Pop : NavigationAction
}