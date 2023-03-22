package com.example.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Adds appId query to all api requests.
 */
class AppIdInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val url = request.url.newBuilder().addQueryParameter(QUERY_APPID, APP_ID).build()
        requestBuilder.url(url)
        return chain.proceed(request = requestBuilder.build())
    }

    private companion object {
        const val QUERY_APPID = "appid"

        //fixme just hardcoded api id for now. Can be provided via gradle, strings, etc.
        const val APP_ID = "4bf2ac55b744ad7ee6da5db1b0b6b7ad"
    }
}