package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.timer.legacy.LegacyTimerRepositoryMigrator
import com.maxpoliakov.skillapp.data.timer.legacy.LegacyTimerRepositoryMigratorImpl
import com.maxpoliakov.skillapp.domain.repository.LegacyTimerRepository
import com.maxpoliakov.skillapp.data.timer.legacy.SharedPreferencesLegacyTimerRepository
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
    fun provideLegacyStopwatchRepository(persistence: SharedPreferencesLegacyTimerRepository): LegacyTimerRepository

    @Binds
    fun provideLegacyTimerRepositoryMigrator(timerMigrator: LegacyTimerRepositoryMigratorImpl) : LegacyTimerRepositoryMigrator
}
