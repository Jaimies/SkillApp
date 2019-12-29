package com.jdevs.timeo.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.DefaultTimeoRepository
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.local.LocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.CollectionMonitor
import com.jdevs.timeo.data.source.remote.ItemsLiveData
import com.jdevs.timeo.data.source.remote.RemoteDataSource
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.RoomConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(context: Context, authRepository: AuthRepository): TimeoRepository {

        return DefaultTimeoRepository(
            provideRemoteDataSource(authRepository),
            provideLocalDataSource(context),
            authRepository
        )
    }

    private fun provideRemoteDataSource(authRepository: AuthRepository): TimeoDataSource {

        return RemoteDataSource(
            CollectionMonitor(ActivitiesConstants.PAGE_SIZE, ::createActivitiesLiveData),
            CollectionMonitor(RecordsConstants.PAGE_SIZE, ::createRecordsLiveData),
            authRepository
        )
    }

    private fun provideLocalDataSource(context: Context): TimeoDataSource {

        val database = Room.databaseBuilder(
            context.applicationContext,
            TimeoDatabase::class.java, DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

        return LocalDataSource(database.activitiesDao(), database.recordsDao())
    }

    private fun createActivitiesLiveData(
        query: Query,
        setLastVisibleItem: (DocumentSnapshot) -> Unit,
        onLastItemReached: () -> Unit
    ): ItemsLiveData {

        return ItemsLiveData(
            query,
            setLastVisibleItem,
            onLastItemReached,
            Activity::class.java,
            ActivitiesConstants.PAGE_SIZE
        )
    }

    private fun createRecordsLiveData(
        query: Query,
        setLastVisibleItem: (DocumentSnapshot) -> Unit,
        onLastItemReached: () -> Unit
    ): ItemsLiveData {

        return ItemsLiveData(
            query,
            setLastVisibleItem,
            onLastItemReached,
            Record::class.java,
            RecordsConstants.PAGE_SIZE
        )
    }
}
