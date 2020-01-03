package com.jdevs.timeo.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.MonthStats
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.WeekStats
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.DefaultTimeoRepository
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.local.LocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.CollectionMonitor
import com.jdevs.timeo.data.source.remote.DefaultRemoteDataSource
import com.jdevs.timeo.data.source.remote.ItemsLiveData
import com.jdevs.timeo.data.source.remote.TimeoRemoteDataSource
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.RoomConstants.DATABASE_NAME
import com.jdevs.timeo.util.StatsConstants
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

    private fun provideRemoteDataSource(authRepository: AuthRepository): TimeoRemoteDataSource {

        return DefaultRemoteDataSource(
            createCollectionMonitor(Activity::class.java, ActivitiesConstants.PAGE_SIZE),
            createCollectionMonitor(Record::class.java, RecordsConstants.PAGE_SIZE),
            createCollectionMonitor(DayStats::class.java, StatsConstants.PAGE_SIZE, true),
            createCollectionMonitor(WeekStats::class.java, StatsConstants.PAGE_SIZE, true),
            createCollectionMonitor(MonthStats::class.java, StatsConstants.PAGE_SIZE, true),
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

    private fun createCollectionMonitor(
        type: Class<out ViewItem>,
        pageSize: Long,
        orderById: Boolean = false
    ): CollectionMonitor {

        return CollectionMonitor(pageSize, createLiveData(type, pageSize), orderById)
    }

    private fun createLiveData(
        type: Class<out ViewItem>,
        pageSize: Long
    ): (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData =
        { query, setLastVisibleItem, onLastReached ->

            ItemsLiveData(query, setLastVisibleItem, onLastReached, type, pageSize)
        }
}
