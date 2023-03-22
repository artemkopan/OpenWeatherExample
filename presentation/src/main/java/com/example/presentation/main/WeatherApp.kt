package com.example.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.presentation.navigation.NavigationAction
import com.example.presentation.navigation.NavigationScreen
import com.example.presentation.navigation.Router
import com.example.presentation.theme.AppTheme

@Composable
fun WeatherApp(
    router: Router,
    viewModel: WeatherAppViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    NavigationLaunchedEffect(router = router, controller = navController)

    AppTheme {
        NavHost(navController = navController, startDestination = state.startDestination) {
            state.screens.forEach { screen ->
                when (val type = screen.type) {
                    NavigationScreen.Type.Composable -> {
                        composable(
                            route = screen.name,
                            content = screen.content,
                            arguments = screen.arguments
                        )
                    }

                    is NavigationScreen.Type.Dialog -> {
                        dialog(
                            route = screen.name,
                            content = screen.content,
                            arguments = screen.arguments,
                            dialogProperties = type.properties
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationLaunchedEffect(router: Router, controller: NavHostController) {
    LaunchedEffect(true) {
        router.navigationFlow.collect {
            when (it) {
                is NavigationAction.Navigate -> {
                    controller.navigate(it.screen, navOptions = it.options)
                }

                NavigationAction.Pop -> controller.popBackStack()
            }
        }
    }
}