package com.jdevs.timeo.data.source.local

import com.jdevs.timeo.data.Activity

class ActivityRepository(private val activityDao: ActivityDao) {

    val activities = activityDao.getActivities()

    suspend fun insert(activity: Activity) {
        activityDao.insert(activity)
    }
}
