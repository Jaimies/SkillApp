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
) : Repository<ActivitiesDataSource, ActivitiesRemoteDataSource>(
    remoteDataSource, localDataSource, authRepository
), ActivitiesRepository {

    override val activities get() = localDataSource.activities
    override val activitiesRemote get() = remoteDataSource.activities
    override val topActivities get() = currentDataSource.getTopActivities()

    override fun getActivityById(id: String) = currentDataSource.getActivityById(id)

    override suspend fun addActivity(name: String) = currentDataSource.addActivity(name)

    override suspend fun saveActivity(activity: Activity) = currentDataSource.saveActivity(activity)

    override suspend fun deleteActivity(activity: Activity) =
        currentDataSource.deleteActivity(activity)
}
