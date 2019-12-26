package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import javax.inject.Inject

class DefaultTimeoRepository @Inject constructor(
    private val remoteDataSource: TimeoDataSource,
    private val localDataSource: TimeoDataSource
) : TimeoRepository {

    private val isUserSignedIn
        get() = AuthRepository.isUserSignedIn

    private val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource

    override val activitiesLiveData get() = currentDataSource.activitiesLiveData
    override val recordsLiveData get() = currentDataSource.recordsLiveData

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        return currentDataSource.getActivityById(id, documentId)
    }

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

    override fun resetRemoteDataSource() {

        if (isUserSignedIn) {

            remoteDataSource.reset()
        }
    }

    override fun resetRecordsMonitor() {

        if (isUserSignedIn) {

            remoteDataSource.resetRecordsMonitor()
        }
    }
}
