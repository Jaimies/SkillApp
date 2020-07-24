package com.jdevs.timeo.data.di

import android.content.Context
import com.jdevs.timeo.data.db.TimeoDatabase
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
    fun provideDatabase(@ApplicationContext context: Context) = TimeoDatabase.create(context)

    @Provides
    @Singleton
    fun provideActivitiesDao(db: TimeoDatabase) = db.activitiesDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: TimeoDatabase) = db.statsDao()

    @Provides
    @Singleton
    fun provideProjectsDao(db: TimeoDatabase) = db.projectsDao()
}
