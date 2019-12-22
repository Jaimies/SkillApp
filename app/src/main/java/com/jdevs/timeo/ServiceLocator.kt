package com.jdevs.timeo

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jdevs.timeo.data.source.ActivitiesDataSource
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.DefaultActivitiesRepository
import com.jdevs.timeo.data.source.local.ActivitiesDatabase
import com.jdevs.timeo.data.source.local.ActivitiesLocalDataSource
import com.jdevs.timeo.data.source.remote.RemoteRepository

object ServiceLocator {

    private val lock = Any()
    private var database: ActivitiesDatabase? = null

    @Volatile
    var activitiesRepository: ActivitiesRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): ActivitiesRepository {
        synchronized(this) {
            return activitiesRepository ?: activitiesRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): ActivitiesRepository {
        return DefaultActivitiesRepository(RemoteRepository, createTaskLocalDataSource(context))
    }

    private fun createTaskLocalDataSource(context: Context): ActivitiesDataSource {
        val database = database ?: createDataBase(context)
        return ActivitiesLocalDataSource(database.activityDao())
    }

    private fun createDataBase(context: Context): ActivitiesDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ActivitiesDatabase::class.java, "activities"
        ).build()
        database = result
        return result
    }

//    @VisibleForTesting
//    fun resetRepository() {
//        synchronized(lock) {
//            runBlocking {
//                FakeTasksRemoteDataSource.deleteAllTasks()
//            }
//            // Clear all data to avoid test pollution.
//            database?.apply {
//                clearAllTables()
//                close()
//            }
//            database = null
//            activitiesRepository = null
//        }
//    }
}