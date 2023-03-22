package com.example.domain.error

interface ErrorHandler {

    fun formatDisplayError(throwable: Throwable): String
}