package com.example.openweather.di

import com.example.domain.converter.DataConverter
import com.example.domain.converter.DataConverterImpl
import com.example.domain.dispatcher.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun providesDataConverter(impl: DataConverterImpl): DataConverter = impl

    @Provides
    @Singleton
    fun providesDispatchersProvider(): DispatchersProvider = object : DispatchersProvider {}
}