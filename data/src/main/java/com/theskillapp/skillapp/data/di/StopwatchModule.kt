package com.theskillapp.skillapp.data.di

import com.theskillapp.skillapp.data.timer.legacy.LegacyTimerRepositoryMigrator
import com.theskillapp.skillapp.data.timer.legacy.LegacyTimerRepositoryMigratorImpl
import com.theskillapp.skillapp.domain.repository.LegacyTimerRepository
import com.theskillapp.skillapp.data.timer.legacy.SharedPreferencesLegacyTimerRepository
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

    @Binds
    fun provideLegacyStopwatchRepository(persistence: SharedPreferencesLegacyTimerRepository): LegacyTimerRepository

    @Binds
    fun provideLegacyTimerRepositoryMigrator(timerMigrator: LegacyTimerRepositoryMigratorImpl) : LegacyTimerRepositoryMigrator
}
