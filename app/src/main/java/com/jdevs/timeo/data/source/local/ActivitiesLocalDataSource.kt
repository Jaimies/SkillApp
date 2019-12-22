package com.jdevs.timeo.data.source.local

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesDataSource

class ActivitiesLocalDataSource(private val activityDao: ActivityDao) : ActivitiesDataSource {

    override val activities = run {
        activityDao.getActivities()
    }

    override suspend fun addActivity(activity: Activity) {

        activityDao.insert(activity)
    }
}
