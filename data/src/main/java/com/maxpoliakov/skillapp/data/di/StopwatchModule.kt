package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.data.stopwatch.SharedPreferencesStopwatchRepository
import com.maxpoliakov.skillapp.domain.stopwatch.StopwatchImpl
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StopwatchModule {
    @Binds
    fun provideStopwatch(stopwatchUtil: StopwatchImpl): Stopwatch

    @Binds
    fun provideStopwatchRepository(persistence: SharedPreferencesStopwatchRepository): StopwatchRepository
}
