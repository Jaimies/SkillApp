package com.jdevs.timeo.domain.repository

import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivitiesRepository {
    val activities: DataSource.Factory<Int, Activity>
    val topActivities: Flow<List<Activity>>

    fun getParentActivitySuggestions(activityId: Int): Flow<List<Activity>>
    fun getActivityById(id: Int): Flow<Activity>

    suspend fun addActivity(activity: Activity)
    suspend fun saveActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
    suspend fun increaseTime(id: Int, time: Int)
}
