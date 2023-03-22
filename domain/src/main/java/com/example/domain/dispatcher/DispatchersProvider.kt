package com.example.domain.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {

    val io: CoroutineDispatcher get() = Dispatchers.IO
    val main: CoroutineDispatcher get() = Dispatchers.Main.immediate
    val default: CoroutineDispatcher get() = Dispatchers.Default
}