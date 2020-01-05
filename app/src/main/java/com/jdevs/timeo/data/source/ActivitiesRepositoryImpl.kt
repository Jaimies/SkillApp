package com.jdevs.timeo.data.source

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.remote.ActivitiesRemoteDataSource
import javax.inject.Inject

class ActivitiesRepositoryImpl @Inject constructor(
    private val remoteDataSource: ActivitiesRemoteDataSource,
    private val localDataSource: TimeoDataSource,
    authRepository: AuthRepository
) : BaseRepository(authRepository), ActivitiesRepository {

    private val currentDataSource get() = if (isUserSignedIn) remoteDataSource else localDataSource

    override val activities get() = currentDataSource.activities

    override fun getActivityById(id: Int, documentId: String) =
        currentDataSource.getActivityById(id, documentId)

    override suspend fun addActivity(activity: Activity) = currentDataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity) = currentDataSource.saveActivity(activity)

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)

    override fun increaseTime(activityId: String, time: Long, batch: WriteBatch) {

        if (isUserSignedIn) {

            remoteDataSource.increaseTime(activityId, time, batch)
        }
    }

    override fun resetActivitiesMonitor() = performOnRemote { it.resetActivitiesMonitor() }
}
