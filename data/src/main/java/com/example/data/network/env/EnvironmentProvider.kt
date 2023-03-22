package com.example.data.network.env

import javax.inject.Inject

/**
 * Provides base url for api methods and for icons.
 * Implementation can be different based on requirements, e.g.: dev, test, prod, etc..
 */
interface EnvironmentProvider {

    fun getBaseUrl(): String

    fun getIconUrl(id: String): String
}

class EnvironmentProviderImpl @Inject constructor() : EnvironmentProvider {

    override fun getBaseUrl(): String {
        return "https://api.openweathermap.org/"
    }

    override fun getIconUrl(id: String): String {
        return "https://openweathermap.org/img/wn/$id@2x.png"
    }
}