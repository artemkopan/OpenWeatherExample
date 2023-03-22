package com.example.data.di

import com.example.data.error.ErrorHandlerImpl
import com.example.data.network.client.ForecastRestClient
import com.example.data.network.env.EnvironmentProvider
import com.example.data.network.env.EnvironmentProviderImpl
import com.example.data.network.interceptor.AppIdInterceptor
import com.example.data.repo.ForecastRepoImpl
import com.example.domain.error.ErrorHandler
import com.example.domain.repo.ForecastRepo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    fun providesWeatherRepo(impl: ForecastRepoImpl): ForecastRepo = impl

    @Provides
    fun providesEnvironment(impl: EnvironmentProviderImpl): EnvironmentProvider = impl

    @Provides
    fun providesGson() = Gson()

    @Provides
    fun providesOkHttp(
        appIdInterceptor: AppIdInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(appIdInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    fun providesRetrofit(
        environmentProvider: EnvironmentProvider,
        gson: Gson,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(environmentProvider.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    fun providesWeatherRestClient(retrofit: Retrofit): ForecastRestClient {
        return retrofit.create(ForecastRestClient::class.java)
    }

    @Provides
    fun providesErrorHandler(impl: ErrorHandlerImpl): ErrorHandler = impl
}