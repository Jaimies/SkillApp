package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultTimeoRepository(
    private val remoteDataSource: TimeoDataSource,
    private val localDataSource: TimeoDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TimeoRepository {

    private val isUserSignedIn get() = AuthRepository.isUserSignedIn

    override suspend fun addActivity(activity: Activity) {

        if (isUserSignedIn) {

            remoteDataSource.addActivity(activity)
        } else {

            localDataSource.addActivity(activity)
        }
    }

    override suspend fun saveActivity(activity: Activity) {

        if (isUserSignedIn) {

            remoteDataSource.saveActivity(activity)
        } else {

            localDataSource.saveActivity(activity)
        }
    }

    override suspend fun deleteActivity(activity: Activity) {

        if (isUserSignedIn) {

            remoteDataSource.deleteActivity(activity)
        } else {

            localDataSource.deleteActivity(activity)
        }
    }

    override suspend fun addRecord(record: Record) {

        if (isUserSignedIn) {

            remoteDataSource.addRecord(record)
        } else {

            localDataSource.addRecord(record)
        }
    }

    override suspend fun deleteRecord(record: Record) {

        if (isUserSignedIn) {

            remoteDataSource.deleteRecord(record)
        } else {

            localDataSource.deleteRecord(record)
        }
    }

    override fun getActivities() = localDataSource.activities
    override fun getRecords(): LiveData<List<Record>> = localDataSource.records
}