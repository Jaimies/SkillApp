package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Activity

interface ActivitiesRepository {

    val activities: LiveData<*>?

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity): WriteBatch

    suspend fun deleteActivity(activity: Activity)

    fun increaseTime(activityId: String, time: Long, batch: WriteBatch)

    fun resetActivitiesMonitor()
}
