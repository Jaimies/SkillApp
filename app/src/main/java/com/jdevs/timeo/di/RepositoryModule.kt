package com.jdevs.timeo.di

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
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(context: Context): TimeoRepository {

        return DefaultTimeoRepository(
            provideRemoteDataSource(),
            provideLocalDataSource(context)
        )
    }

    private fun provideRemoteDataSource(): TimeoDataSource {

        return RemoteDataSource(
            CollectionMonitor(ActivitiesConstants.FETCH_LIMIT, ::ActivitiesLiveData),
            CollectionMonitor(RecordsConstants.FETCH_LIMIT, ::RecordsLiveData)
        )
    }

    private fun provideLocalDataSource(context: Context): TimeoDataSource {

        val database = Room.databaseBuilder(
            context.applicationContext,
            TimeoDatabase::class.java, "timeo"
        ).build()

        return LocalDataSource(database.activitiesDao(), database.recordsDao())
    }
}
