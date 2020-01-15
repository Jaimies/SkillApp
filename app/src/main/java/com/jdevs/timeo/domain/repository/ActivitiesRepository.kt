package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.domain.model.Activity

interface ActivitiesRepository {

    val activities: LiveData<*>?

    val topActivities: LiveData<List<Activity>>

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity): WriteBatch?

    suspend fun deleteActivity(activity: Activity)

    suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch)

    fun resetMonitor()
}