package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.di.StopwatchModule
import com.maxpoliakov.skillapp.domain.repository.StopwatchRepository
import com.maxpoliakov.skillapp.data.stopwatch.StopwatchRepositoryImpl
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
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

    @Binds
    fun provideStopwatchRepository(persistence: StopwatchRepositoryImpl): StopwatchRepository
}
