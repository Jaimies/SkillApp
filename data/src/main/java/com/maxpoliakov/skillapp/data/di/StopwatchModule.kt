package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.stopwatch.StopwatchPersistence
import com.maxpoliakov.skillapp.data.stopwatch.StopwatchPersistenceImpl
import com.maxpoliakov.skillapp.data.stopwatch.StopwatchUtilImpl
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StopwatchModule {
    @Binds
    fun provideStopwatchUtil(stopwatchUtil: StopwatchUtilImpl): StopwatchUtil

    @Binds
    fun provideStopwatchPersistence(persistence: StopwatchPersistenceImpl): StopwatchPersistence
}
