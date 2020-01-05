package com.jdevs.timeo.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.RecordsRepository
import com.jdevs.timeo.data.source.RecordsRepositoryImpl
import com.jdevs.timeo.data.source.local.StatsLocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.CollectionMonitor
import com.jdevs.timeo.data.source.remote.ItemsLiveData
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSource
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSourceImpl
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.RoomConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(context: Context, authRepository: AuthRepository): RecordsRepository {

        return RecordsRepositoryImpl(
            provideRemoteDataSource(authRepository),
            provideLocalDataSource(context),
            authRepository
        )
    }

    private fun provideRemoteDataSource(authRepository: AuthRepository): RecordsRemoteDataSource {

        return RecordsRemoteDataSourceImpl(
            createCollectionMonitor(Record::class.java, RecordsConstants.PAGE_SIZE),
            authRepository
        )
    }

    private fun provideLocalDataSource(context: Context): StatsLocalDataSource {

        val database = Room.databaseBuilder(
            context.applicationContext,
            TimeoDatabase::class.java, DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

        return StatsLocalDataSource(database)
    }

    private fun createCollectionMonitor(
        type: Class<out ViewItem>,
        pageSize: Long,
        orderBy: String = TIMESTAMP_PROPERTY
    ) = CollectionMonitor(pageSize, createLiveData(type, pageSize), orderBy)

    private fun createLiveData(
        type: Class<out ViewItem>,
        pageSize: Long
    ): (Query, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData =
        { query, setLastVisibleItem, onLastReached ->

            ItemsLiveData(query, setLastVisibleItem, onLastReached, type, pageSize)
        }
}
