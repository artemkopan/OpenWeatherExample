package com.example.presentation.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() {

    private val navigationChannel = Channel<NavigationAction>(BUFFERED)
    val navigationFlow = navigationChannel.receiveAsFlow()

    suspend fun route(action: NavigationAction) {
        navigationChannel.send(action)
    }
}