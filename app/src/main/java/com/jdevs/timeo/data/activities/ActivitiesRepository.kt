package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Repository
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.model.Activity
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesRepository {

    val activities: LiveData<*>?

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity): WriteBatch?

    suspend fun deleteActivity(activity: Activity)

    suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch)

    fun resetMonitor()
}

@Singleton
class DefaultActivitiesRepository @Inject constructor(
    private val remoteDataSource: ActivitiesRemoteDataSource,
    private val localDataSource: ActivitiesLocalDataSource,
    authRepository: AuthRepository
) : Repository<ActivitiesDataSource, ActivitiesRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
), ActivitiesRepository {

    override val activities get() = currentDataSource.activities

    override fun getActivityById(id: Int, documentId: String) =
        currentDataSource.getActivityById(id, documentId)

    override suspend fun addActivity(activity: Activity) = currentDataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity): WriteBatch? {

        return if (isUserSignedIn) {

            localDataSource.saveActivity(activity)
            null
        } else {

            remoteDataSource.saveActivity(activity.documentId, activity.name)
        }
    }

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)

    override suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch) =
        performOnRemoteSuspend {

            it.increaseTime(activityId, time, batch)
        }

    override fun resetMonitor() = performOnRemote { it.resetMonitor() }
}
