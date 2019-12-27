package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import javax.inject.Inject

class FakeAndroidTestRepository @Inject constructor() : TimeoRepository {

    override val activities = MutableLiveData(mutableListOf<Activity>())
    override val records = MutableLiveData(mutableListOf<Record>())

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        val activity = activities.value
            ?.singleOrNull { it.id == id || it.documentId == documentId }
            ?: Activity()

        return MutableLiveData(activity)
    }

    override suspend fun addActivity(activity: Activity) {

        activities.value?.add(activity)
    }

    override suspend fun addRecord(record: Record) {

        records.value?.add(record)
    }

    override suspend fun deleteActivity(activity: Activity) {

        activities.value?.remove(activity)
    }

    override suspend fun deleteRecord(record: Record) {

        records.value?.remove(record)
    }

    override suspend fun saveActivity(activity: Activity) {

        val index = activities.value?.indexOfFirst { it.id == activity.id } ?: return
        activities.value?.set(index, activity)
    }

    override fun resetRecordsMonitor() {}

    override fun resetRemoteDataSource() {}
}