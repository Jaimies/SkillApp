package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation

interface ActivitiesRepository {

    val activities: DataSource.Factory<Int, Activity>
    val activitiesRemote: List<LiveData<Operation<Activity>>>

    val topActivities: LiveData<List<Activity>>

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(name: String)

    suspend fun saveActivity(activity: Activity): WriteBatch?

    suspend fun deleteActivity(activity: Activity)

    suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch)
}
