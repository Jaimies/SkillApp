package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record

interface TimeoRepository {

    val activities: LiveData<*>?
    val records: LiveData<*>?

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun addRecord(record: Record)

    suspend fun deleteRecord(record: Record)

    fun resetActivitiesMonitor()
    fun resetRecordsMonitor()
}
