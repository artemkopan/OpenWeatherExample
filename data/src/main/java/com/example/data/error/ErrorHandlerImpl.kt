package com.example.data.error

import android.content.Context
import com.example.data.R
import com.example.domain.error.ErrorHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : ErrorHandler {

    override fun formatDisplayError(throwable: Throwable): String {
        return when {
            //fixme implement different type throwable
            throwable is UnknownHostException -> {
                context.getString(R.string.no_internet_connection)
            }

            throwable.message.isNullOrBlank() -> context.getString(R.string.unknown_error)
            else -> throwable.message.orEmpty()
        }
    }
}