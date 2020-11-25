package com.jdevs.timeo.domain.repository

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Id
import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface ActivitiesRepository {
    fun getActivities(): Flow<List<Activity>>

    fun getParentActivitySuggestions(activityId: Id): Flow<List<Activity>>
    fun getActivityById(id: Id): Flow<Activity>

    suspend fun addActivity(activity: Activity)
    suspend fun saveActivity(activity: Activity)
    suspend fun deleteActivity(activity: Activity)
    suspend fun increaseTime(id: Id, time: Duration)
    suspend fun decreaseTime(id: Id, time: Duration)
}
