package com.theskillapp.skillapp.data.di

import com.theskillapp.skillapp.domain.stopwatch.StopwatchImpl
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StopwatchModule {
    @Binds
    fun provideStopwatch(stopwatchUtil: StopwatchImpl): Stopwatch
}
