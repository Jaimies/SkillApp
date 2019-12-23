package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record

class DefaultTimeoRepository(
    private val remoteDataSource: TimeoDataSource,
    private val localDataSource: TimeoDataSource
) : TimeoRepository {

    override val activitiesLiveData: LiveData<*>?
        get() = if (isUserSignedIn) mActivitiesLiveData else activities

    override val recordsLiveData: LiveData<*>?
        get() = if (isUserSignedIn) mRecordsLiveData else records

    private val isUserSignedIn
        get() = AuthRepository.isUserSignedIn

    private val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource

    private val activities = localDataSource.activities
    private val records = localDataSource.records
    private val mActivitiesLiveData get() = remoteDataSource.activitiesLiveData
    private val mRecordsLiveData get() = remoteDataSource.recordsLiveData

    override suspend fun addActivity(activity: Activity) {

        currentDataSource.addActivity(activity)
    }

    override suspend fun saveActivity(activity: Activity) {

        currentDataSource.saveActivity(activity)
    }

    override suspend fun deleteActivity(activity: Activity) {

        currentDataSource.deleteActivity(activity)
    }

    override suspend fun addRecord(record: Record) {

        currentDataSource.addRecord(record)
    }

    override suspend fun deleteRecord(record: Record) {

        currentDataSource.deleteRecord(record)
    }
}
