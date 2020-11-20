package com.jdevs.timeo.domain.repository

import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Id
import kotlinx.coroutines.flow.Flow

interface ActivitiesRepository {
    val activities: DataSource.Factory<Id, Activity>

    fun getParentActivitySuggestions(activityId: Id): Flow<List<Activity>>
    fun getActivityById(id: Id): Flow<Activity>

    suspend fun addActivity(activity: Activity)
    suspend fun saveActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
    suspend fun increaseTime(id: Id, time: Int)
}
