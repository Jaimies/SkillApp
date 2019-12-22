package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record

interface TimeoRepository {

    fun getActivities(): LiveData<List<Activity>>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    fun getRecords(): LiveData<List<Record>>

    suspend fun addRecord(record: Record)

    suspend fun deleteRecord(record: Record)
}