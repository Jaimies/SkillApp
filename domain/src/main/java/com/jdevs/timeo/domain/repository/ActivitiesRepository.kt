package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Activity

interface ActivitiesRepository {
    val activities: DataSource.Factory<Int, Activity>
    val topActivities: LiveData<List<Activity>>

    fun getParentActivitySuggestions(activityId: Int): LiveData<List<Activity>>
    fun getActivityById(id: Int): LiveData<Activity>

    suspend fun addActivity(activity: Activity)
    suspend fun saveActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
}
