package com.jdevs.timeo.data.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultActivitiesRepository @Inject constructor(
    private val activitiesDao: ActivitiesDao
) : ActivitiesRepository {

    override fun getParentActivitySuggestions(activityId: Int): Flow<List<Activity>> {
        return activitiesDao.getParentActivitySuggestions(activityId)
            .mapList { it.mapToDomain() }
    }

    override val activities by lazy {
        activitiesDao.getActivities().map(DBActivity::mapToDomain)
    }

    override val topActivities
        get() = activitiesDao.getTopActivities().mapList { it.mapToDomain() }

    override fun getActivityById(id: Int) =
        activitiesDao.getActivity(id).map { it.mapToDomain() }

    override suspend fun addActivity(activity: Activity) =
        activitiesDao.insert(activity.mapToDB())

    override suspend fun saveActivity(activity: Activity) =
        activitiesDao.update(activity.mapToDB())

    override suspend fun deleteActivity(activity: Activity) =
        activitiesDao.delete(activity.mapToDB())
}
