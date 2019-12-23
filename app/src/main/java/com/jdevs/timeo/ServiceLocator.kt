package com.jdevs.timeo

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jdevs.timeo.data.source.DefaultTimeoRepository
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.local.LocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.ItemsLiveData.ActivitiesLiveData
import com.jdevs.timeo.data.source.remote.ItemsLiveData.RecordsLiveData
import com.jdevs.timeo.data.source.remote.RemoteDataSource
import com.jdevs.timeo.data.source.remote.TimeoRemoteDataSource
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants

object ServiceLocator {

    private var localDatabase: TimeoDatabase? = null

    @Volatile
    var timeoRepository: TimeoRepository? = null
        @VisibleForTesting set

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

        return TimeoRemoteDataSource(
            RemoteDataSource(ActivitiesConstants.FETCH_LIMIT, ::ActivitiesLiveData),
            RemoteDataSource(RecordsConstants.FETCH_LIMIT, ::RecordsLiveData)
        )
    }

    private fun createDataBase(context: Context): TimeoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            TimeoDatabase::class.java, "activities"
        ).build()
        localDatabase = result
        return result
    }
}
