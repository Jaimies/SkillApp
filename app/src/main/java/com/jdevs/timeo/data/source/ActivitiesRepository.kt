package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity

interface ActivitiesRepository {

    suspend fun addActivity(activity: Activity)
    fun getActivities(): LiveData<List<Activity>>
}