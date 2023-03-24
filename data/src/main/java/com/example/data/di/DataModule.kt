package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.ForecastDatabase
import com.example.data.database.dao.ForecastDao
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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


    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): ForecastDatabase {
        return Room.databaseBuilder(
            context,
            ForecastDatabase::class.java,
            "forecast_db"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: ForecastDatabase) = db.forecastDao()
}