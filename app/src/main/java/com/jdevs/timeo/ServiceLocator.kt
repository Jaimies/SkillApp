package com.jdevs.timeo

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jdevs.timeo.data.source.DefaultTimeoRepository
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.local.LocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.RemoteRepository

object ServiceLocator {

    private var timeoDatabase: TimeoDatabase? = null

    @Volatile
    var timeoRepository: TimeoRepository? = null
        @VisibleForTesting set

    fun provideLocalRepository(context: Context): TimeoRepository {
        synchronized(this) {
            return timeoRepository ?: createActivitiesRepository(context)
        }
    }

    private fun createActivitiesRepository(context: Context): TimeoRepository {
        return DefaultTimeoRepository(RemoteRepository, createActivityLocalDataSource(context))
    }

    private fun createActivityLocalDataSource(context: Context): TimeoDataSource {
        val database = timeoDatabase ?: createDataBase(context)
        return LocalDataSource(database.activityDao(), database.recordsDao())
    }

    private fun createDataBase(context: Context): TimeoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            TimeoDatabase::class.java, "activities"
        ).build()
        timeoDatabase = result
        return result
    }
}