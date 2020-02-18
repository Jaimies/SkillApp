package com.jdevs.timeo.data.di

import android.content.Context
import com.jdevs.timeo.data.db.TimeoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = TimeoDatabase.create(context)

    @Provides
    @Singleton
    fun provideActivitiesDao(db: TimeoDatabase) = db.activitiesDao()

    @Provides
    @Singleton
    fun provideTasksDao(db: TimeoDatabase) = db.tasksDao()

    @Provides
    @Singleton
    fun provideStatsDao(db: TimeoDatabase) = db.statsDao()

    @Provides
    @Singleton
    fun provideProjectsDao(db: TimeoDatabase) = db.projectsDao()
}
