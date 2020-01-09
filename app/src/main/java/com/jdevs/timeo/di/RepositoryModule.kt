package com.jdevs.timeo.di

import android.content.Context
import androidx.room.Room
import com.jdevs.timeo.data.activities.ActivitiesLocalDataSource
import com.jdevs.timeo.data.activities.ActivitiesRemoteDataSource
import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.data.activities.DefaultActivitiesRepository
import com.jdevs.timeo.data.activities.FirestoreActivitiesDataSource
import com.jdevs.timeo.data.activities.RoomActivitiesDataSource
import com.jdevs.timeo.data.db.TimeoDatabase
import com.jdevs.timeo.data.projects.DefaultProjectsRepository
import com.jdevs.timeo.data.projects.FirestoreProjectsDataSource
import com.jdevs.timeo.data.projects.ProjectsLocalDataSource
import com.jdevs.timeo.data.projects.ProjectsRemoteDataSource
import com.jdevs.timeo.data.projects.ProjectsRepository
import com.jdevs.timeo.data.projects.RoomProjectsDataSource
import com.jdevs.timeo.data.records.DefaultRecordsRepository
import com.jdevs.timeo.data.records.FirestoreRecordsDataSource
import com.jdevs.timeo.data.records.RecordsDataSource
import com.jdevs.timeo.data.records.RecordsRemoteDataSource
import com.jdevs.timeo.data.records.RecordsRepository
import com.jdevs.timeo.data.records.RoomRecordsDataSource
import com.jdevs.timeo.data.stats.DefaultStatsRepository
import com.jdevs.timeo.data.stats.FirestoreStatsDataSource
import com.jdevs.timeo.data.stats.RoomStatsDataSource
import com.jdevs.timeo.data.stats.StatsDataSource
import com.jdevs.timeo.data.stats.StatsRemoteDataSource
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
    abstract fun provideProjectsRepository(repository: DefaultProjectsRepository): ProjectsRepository

    @Binds
    abstract fun provideRecordsRepository(repository: DefaultRecordsRepository): RecordsRepository

    @Binds
    abstract fun provideStatsRepository(repository: DefaultStatsRepository): StatsRepository

    @Binds
    abstract fun provideActivitiesRemoteDataSource(source: FirestoreActivitiesDataSource): ActivitiesRemoteDataSource

    @Binds
    abstract fun provideProjectsRemoteDataSource(source: FirestoreProjectsDataSource): ProjectsRemoteDataSource

    @Binds
    abstract fun provideRecordsRemoteDataSource(source: FirestoreRecordsDataSource): RecordsRemoteDataSource

    @Binds
    abstract fun provideStatsRemoteDataSource(source: FirestoreStatsDataSource): StatsRemoteDataSource

    @Binds
    abstract fun provideActivitiesLocalDataSource(source: RoomActivitiesDataSource): ActivitiesLocalDataSource

    @Binds
    abstract fun provideProjectsLocalDataSource(source: RoomProjectsDataSource): ProjectsLocalDataSource

    @Binds
    abstract fun provideRecordsLocalDataSource(source: RoomRecordsDataSource): RecordsDataSource

    @Binds
    abstract fun provideStatsLocalDataSource(source: RoomStatsDataSource): StatsDataSource

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
        fun provideActivitiesDao(db: TimeoDatabase) = db.activitiesDao()

        @Provides
        @Singleton
        @JvmStatic
        fun provideStatsDao(db: TimeoDatabase) = db.statsDao()

        @Provides
        @Singleton
        @JvmStatic
        fun provideProjectsDao(db: TimeoDatabase) = db.projectsDao()
    }
}
