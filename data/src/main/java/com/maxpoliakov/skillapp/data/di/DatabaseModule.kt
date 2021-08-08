package com.maxpoliakov.skillapp.data.di

import android.content.Context
import com.maxpoliakov.skillapp.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.create(context)
    }

    @Provides
    @Singleton
    fun provideSkillDao(db: AppDatabase) = db.skillDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: AppDatabase) = db.statsDao()

    @Provides
    @Singleton
    fun provideRecordsDao(db: AppDatabase) = db.recordsDao()

    @Provides
    @Singleton
    fun provideSkillGroupDao(db: AppDatabase) = db.skillGroupDao()
}
