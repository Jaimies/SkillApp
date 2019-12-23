package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.remote.ItemsLiveData

interface TimeoRepository {

    val activities: LiveData<List<Activity>>
    val activitiesLiveData: ItemsLiveData?
    val records: LiveData<List<Record>>
    val recordsLiveData: ItemsLiveData?

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun addRecord(record: Record)

    suspend fun deleteRecord(record: Record)
}
