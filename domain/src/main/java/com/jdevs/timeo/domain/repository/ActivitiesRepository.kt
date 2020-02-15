package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation

interface ActivitiesRepository {

    val activities: DataSource.Factory<Int, Activity>
    fun getRemoteActivities(fetchNewItems: Boolean): List<LiveData<Operation<Activity>>>

    val topActivities: LiveData<List<Activity>>

    fun getActivityById(id: String): LiveData<Activity>

    suspend fun addActivity(name: String)

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)
}
