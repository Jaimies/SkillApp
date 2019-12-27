package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultTimeoRepository @Inject constructor(
    private val remoteDataSource: TimeoDataSource,
    private val localDataSource: TimeoDataSource,
    private val userManager: UserManager,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TimeoRepository {

    private val isUserSignedIn
        get() = userManager.isUserSignedIn

    private val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource

    override val activities get() = currentDataSource.activitiesLiveData
    override val records get() = currentDataSource.recordsLiveData

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        return currentDataSource.getActivityById(id, documentId)
    }

    override suspend fun addActivity(activity: Activity) = withContext(defaultDispatcher) {

        currentDataSource.addActivity(activity)
    }

    override suspend fun saveActivity(activity: Activity) = withContext(defaultDispatcher) {

        currentDataSource.saveActivity(activity)
    }

    override suspend fun deleteActivity(activity: Activity) = withContext(defaultDispatcher) {

        currentDataSource.deleteActivity(activity)
    }

    override suspend fun addRecord(record: Record) = withContext(defaultDispatcher) {

        currentDataSource.addRecord(record)
    }

    override suspend fun deleteRecord(record: Record) = withContext(defaultDispatcher) {

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
