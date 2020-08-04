package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation

interface ActivitiesRepository {
    val activities: DataSource.Factory<Int, Activity>
    val topActivities: LiveData<List<Activity>>

    fun getRemoteActivities(fetchNewItems: Boolean): List<LiveData<Operation<Activity>>>
    fun getTopLevelActivitiesFromCache(activityIdToExclude: String): LiveData<List<Activity>>
    fun getActivityById(id: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)
    suspend fun saveActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
}
