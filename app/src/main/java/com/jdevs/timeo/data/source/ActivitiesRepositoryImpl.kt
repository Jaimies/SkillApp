package com.jdevs.timeo.data.source

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.remote.ActivitiesRemoteDataSource
import javax.inject.Inject

class ActivitiesRepositoryImpl @Inject constructor(
    remoteDataSource: ActivitiesRemoteDataSource,
    localDataSource: ActivitiesDataSource,
    authRepository: AuthRepository
) : BaseRepository<ActivitiesRemoteDataSource, ActivitiesDataSource>(
    remoteDataSource, localDataSource, authRepository
), ActivitiesRepository {

    override val activities get() = currentDataSource.activities

    override fun getActivityById(id: Int, documentId: String) =
        currentDataSource.getActivityById(id, documentId)

    override suspend fun addActivity(activity: Activity) = currentDataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity) = currentDataSource.saveActivity(activity)

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)

    override suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch) =
        performOnRemoteSuspend {

            it.increaseTime(activityId, time, batch)
        }

    override fun resetActivitiesMonitor() = performOnRemote { it.resetActivitiesMonitor() }
}
