package com.maxpoliakov.skillapp.di

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtilImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface StopwatchModule {
    @Binds
    fun provideStopwatchUtil(stopwatchUtil: StopwatchUtilImpl): StopwatchUtil
}
