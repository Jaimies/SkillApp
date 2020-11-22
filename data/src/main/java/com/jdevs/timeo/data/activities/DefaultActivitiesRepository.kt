package com.jdevs.timeo.data.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultActivitiesRepository @Inject constructor(
    private val activitiesDao: ActivitiesDao
) : ActivitiesRepository {

    override val activities by lazy {
        activitiesDao.getActivities().mapList { it.mapToDomain() }
    }

    override fun getParentActivitySuggestions(activityId: Int): Flow<List<Activity>> {
        return activitiesDao.getParentActivitySuggestions(activityId)
            .mapList { it.mapToDomain() }
    }

    override fun getActivityById(id: Int) =
        activitiesDao.getActivity(id).map { it.mapToDomain() }

    override suspend fun addActivity(activity: Activity) =
        activitiesDao.insert(activity.mapToDB())

    override suspend fun saveActivity(activity: Activity) =
        activitiesDao.update(activity.mapToDB())

    override suspend fun deleteActivity(activity: Activity) =
        activitiesDao.delete(activity.mapToDB())

    override suspend fun increaseTime(id: Id, time: Duration) {
        activitiesDao.increaseTime(id, time.toMinutes())
    }

    override suspend fun decreaseTime(id: Id, time: Duration) {
        activitiesDao.increaseTime(id, -time.toMinutes())
    }
}
