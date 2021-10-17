package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.di.StopwatchModule
import com.maxpoliakov.skillapp.data.stopwatch.StopwatchPersistence
import com.maxpoliakov.skillapp.data.stopwatch.StopwatchPersistenceImpl
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
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
    fun provideStopwatchUtil(stopwatchUtil: StubStopwatchUtil): StopwatchUtil

    @Binds
    fun provideStopwatchPersistence(persistence: StopwatchPersistenceImpl): StopwatchPersistence
}
