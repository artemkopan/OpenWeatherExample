package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

interface NavigationScreen {
    val name: String
    val content: @Composable (NavBackStackEntry) -> Unit
    val arguments: List<NamedNavArgument> get() = emptyList()
    val type: Type get() = Type.Composable

    sealed interface Type {
        object Composable : Type
        data class Dialog(val properties: DialogProperties = DialogProperties()) : Type
    }
}