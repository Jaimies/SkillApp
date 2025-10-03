package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.data.di.StopwatchModule
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StopwatchModule::class],
)
interface TestStopwatchModule {
    @Binds
    fun provideStopwatch(stopwatchUtil: StubStopwatch): Stopwatch
}
