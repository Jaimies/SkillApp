package com.jdevs.timeo.data.activities

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
) :
    Repository<ActivitiesDataSource>(remoteDataSource, localDataSource, authRepository),
    ActivitiesRepository {

    override val activities get() = localDataSource.activities
    override fun getRemoteActivities(fetchNewItems: Boolean) =
        remoteDataSource.getActivities(fetchNewItems)

    override fun getActivitiesFromCache(activityIdToExclude: String) =
        remoteDataSource.getActivitiesFromCache(activityIdToExclude)

    override val topActivities get() = currentDataSource.getTopActivities()

    override fun getActivityById(id: String) = currentDataSource.getActivityById(id)

    override suspend fun addActivity(activity: Activity) = currentDataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity) = currentDataSource.saveActivity(activity)

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)
}
