package com.example.domain.model

data class DataState<out T>(
    val local: State<T>,
    val remote: State<T>
) {
    data class State<out T>(
        val loading: Boolean = false,
        val result: T? = null,
        val throwable: Throwable? = null
    )
}