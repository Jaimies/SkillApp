package com.jdevs.timeo.data.activities

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultActivitiesRepository @Inject constructor(
    private val remoteDataSource: ActivitiesRemoteDataSource,
    private val localDataSource: ActivitiesLocalDataSource,
    authRepository: AuthRepository
) : Repository<ActivitiesDataSource, ActivitiesRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
), ActivitiesRepository {

    override val activities get() = currentDataSource.activities
    override val topActivities get() = currentDataSource.getTopActivities()

    override fun getActivityById(id: Int, documentId: String) =
        currentDataSource.getActivityById(id, documentId)

    override suspend fun addActivity(activity: Activity) = currentDataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity) = if (isUserSignedIn) {

        remoteDataSource.saveActivity(activity.documentId, activity.name)
    } else {

        localDataSource.saveActivity(activity)
        null
    }

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)

    override suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch) =
        performOnRemoteSuspend {

            it.increaseTime(activityId, time, batch)
        }

    override fun resetMonitor() = performOnRemote { it.resetMonitor() }
}
