package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity

interface ActivitiesRepository {

    fun getActivities(): LiveData<List<Activity>>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)
}