package com.jdevs.timeo.di

import android.content.Context
import androidx.room.Room
import com.jdevs.timeo.data.activities.ActivitiesDataSource
import com.jdevs.timeo.data.activities.ActivitiesLocalDataSource
import com.jdevs.timeo.data.activities.ActivitiesRemoteDataSource
import com.jdevs.timeo.data.activities.ActivitiesRemoteDataSourceImpl
import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.data.activities.DefaultActivitiesRepository
import com.jdevs.timeo.data.db.ActivitiesDao
import com.jdevs.timeo.data.db.StatsDao
import com.jdevs.timeo.data.db.TimeoDatabase
import com.jdevs.timeo.data.records.DefaultRecordsRepository
import com.jdevs.timeo.data.records.RecordsDataSource
import com.jdevs.timeo.data.records.RecordsLocalDataSource
import com.jdevs.timeo.data.records.RecordsRemoteDataSource
import com.jdevs.timeo.data.records.RecordsRemoteDataSourceImpl
import com.jdevs.timeo.data.records.RecordsRepository
import com.jdevs.timeo.data.stats.DefaultStatsRepository
import com.jdevs.timeo.data.stats.StatsDataSource
import com.jdevs.timeo.data.stats.StatsLocalDataSource
import com.jdevs.timeo.data.stats.StatsRemoteDataSource
import com.jdevs.timeo.data.stats.StatsRemoteDataSourceImpl
import com.jdevs.timeo.data.stats.StatsRepository
import com.jdevs.timeo.util.RoomConstants.DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideActivitiesRepository(repository: DefaultActivitiesRepository): ActivitiesRepository

    @Binds
    abstract fun provideRecordsRepository(repository: DefaultRecordsRepository): RecordsRepository

    @Binds
    abstract fun provideStatsRepository(repository: DefaultStatsRepository): StatsRepository

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
