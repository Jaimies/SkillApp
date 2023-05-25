package com.maxpoliakov.skillapp.data.di

import com.maxpoliakov.skillapp.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    fun provideSkillDao(db: AppDatabase) = db.skillDao()

    @Provides
    fun provideStatsDao(db: AppDatabase) = db.statsDao()

    @Provides
    fun provideRecordsDao(db: AppDatabase) = db.recordsDao()

    @Provides
    fun provideSkillGroupDao(db: AppDatabase) = db.skillGroupDao()

    @Provides
    fun provideTimerDao(db: AppDatabase) = db.timerDao()
}
