package com.jdevs.timeo.data.source.local

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesDataSource

class ActivitiesLocalDataSource(private val activityDao: ActivityDao) : ActivitiesDataSource {

    override val activities = activityDao.getActivities()

    override suspend fun addActivity(activity: Activity) = activityDao.insert(activity)

    override suspend fun saveActivity(activity: Activity) = activityDao.update(activity)

    override suspend fun deleteActivity(activity: Activity) = activityDao.delete(activity)
}
