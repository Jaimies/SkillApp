package com.jdevs.timeo.di

import android.content.Context
import androidx.room.Room
import com.jdevs.timeo.data.source.ActivitiesDataSource
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.ActivitiesRepositoryImpl
import com.jdevs.timeo.data.source.RecordsDataSource
import com.jdevs.timeo.data.source.RecordsRepository
import com.jdevs.timeo.data.source.RecordsRepositoryImpl
import com.jdevs.timeo.data.source.StatsDataSource
import com.jdevs.timeo.data.source.StatsRepository
import com.jdevs.timeo.data.source.StatsRepositoryImpl
import com.jdevs.timeo.data.source.local.ActivitiesDao
import com.jdevs.timeo.data.source.local.ActivitiesLocalDataSource
import com.jdevs.timeo.data.source.local.RecordsLocalDataSource
import com.jdevs.timeo.data.source.local.StatsDao
import com.jdevs.timeo.data.source.local.StatsLocalDataSource
import com.jdevs.timeo.data.source.local.TimeoDatabase
import com.jdevs.timeo.data.source.remote.ActivitiesRemoteDataSource
import com.jdevs.timeo.data.source.remote.ActivitiesRemoteDataSourceImpl
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSource
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSourceImpl
import com.jdevs.timeo.data.source.remote.StatsRemoteDataSource
import com.jdevs.timeo.data.source.remote.StatsRemoteDataSourceImpl
import com.jdevs.timeo.util.RoomConstants.DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideActivitiesRepository(repository: ActivitiesRepositoryImpl): ActivitiesRepository

    @Binds
    abstract fun provideRecordsRepository(repository: RecordsRepositoryImpl): RecordsRepository

    @Binds
    abstract fun provideStatsRepository(repository: StatsRepositoryImpl): StatsRepository

    @Binds
    abstract fun provideActivitiesRemoteDataSource(dataSource: ActivitiesRemoteDataSourceImpl): ActivitiesRemoteDataSource

    @Binds
    abstract fun provideRecordsRemoteDataSource(dataSource: RecordsRemoteDataSourceImpl): RecordsRemoteDataSource

    @Binds
    abstract fun provideStatsRemoteDataSource(dataSource: StatsRemoteDataSourceImpl): StatsRemoteDataSource

    @Binds
    abstract fun provideActivitiesLocalDataSource(dataSource: ActivitiesLocalDataSource): ActivitiesDataSource

    @Binds
    abstract fun provideRecordsLocalDataSource(dataSource: RecordsLocalDataSource): RecordsDataSource

    @Binds
    abstract fun provideStatsLocalDataSource(dataSource: StatsLocalDataSource): StatsDataSource

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideDatabase(context: Context): TimeoDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                TimeoDatabase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideActivitiesDao(db: TimeoDatabase): ActivitiesDao = db.activitiesDao()

        @Provides
        @Singleton
        @JvmStatic
        fun provideStatsDao(db: TimeoDatabase): StatsDao = db.statsDao()
    }
}
