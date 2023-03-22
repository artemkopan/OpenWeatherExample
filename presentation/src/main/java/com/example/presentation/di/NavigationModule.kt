package com.example.presentation.di

import com.example.presentation.ui.list.ForecastScreenNav
import com.example.presentation.navigation.NavigationScreen
import com.example.presentation.ui.details.DetailsScreenNav
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @IntoSet
    abstract fun bindsForecastNavigation(impl: ForecastScreenNav): NavigationScreen

    @Binds
    @IntoSet
    abstract fun bindsDetailsScreenNav(impl: DetailsScreenNav): NavigationScreen
}