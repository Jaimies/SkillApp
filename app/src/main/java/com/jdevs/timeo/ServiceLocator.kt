package com.jdevs.timeo

import android.content.Context
import androidx.room.Room
import com.jdevs.timeo.data.source.DefaultTimeoRepository
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.local.LocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.CollectionMonitor
import com.jdevs.timeo.data.source.remote.ItemsLiveData.ActivitiesLiveData
import com.jdevs.timeo.data.source.remote.ItemsLiveData.RecordsLiveData
import com.jdevs.timeo.data.source.remote.RemoteDataSource
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants

object ServiceLocator {

    private var localDatabase: TimeoDatabase? = null
    private var timeoRepository: TimeoRepository? = null

    fun provideRepository(context: Context): TimeoRepository {

        synchronized(this) {
            return timeoRepository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): TimeoRepository {
        return DefaultTimeoRepository(createRemoteDataSource(), createLocalDataSource(context))
    }

    private fun createLocalDataSource(context: Context): TimeoDataSource {
        val database = localDatabase ?: createDataBase(context)
        return LocalDataSource(database.activityDao(), database.recordsDao())
    }

    private fun createRemoteDataSource(): TimeoDataSource {

        return RemoteDataSource(
            CollectionMonitor(ActivitiesConstants.FETCH_LIMIT, ::ActivitiesLiveData),
            CollectionMonitor(RecordsConstants.FETCH_LIMIT, ::RecordsLiveData)
        )
    }

    private fun createDataBase(context: Context): TimeoDatabase {

        Room.databaseBuilder(
            context.applicationContext,
            TimeoDatabase::class.java, "activities"
        ).build().also {

            localDatabase = it
            return it
        }
    }
}
