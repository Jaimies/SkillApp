package com.maxpoliakov.skillapp.data.di

import android.content.Context
import com.maxpoliakov.skillapp.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.create(context)
    }

    @Provides
    @Singleton
    fun provideActivitiesDao(db: AppDatabase) = db.activitiesDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: AppDatabase) = db.statsDao()

    @Provides
    @Singleton
    fun provideRecordsDao(db: AppDatabase) = db.recordsDao()
}
