package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record

class FakeDataSource(
    activityList: List<Activity> = emptyList(),
    recordList: List<Record> = emptyList()
) : TimeoDataSource {

    override val activitiesLiveData: LiveData<MutableList<Activity>>? get() = _activities
    override val recordsLiveData: LiveData<MutableList<Record>>? get() = _records

    private val _activities = MutableLiveData(activityList.toMutableList())
    private val _records = MutableLiveData(recordList.toMutableList())

    override fun getActivityById(id: Int, documentId: String) = MutableLiveData(Activity())
    override suspend fun addActivity(activity: Activity) {}
    override suspend fun addRecord(record: Record) {}
    override suspend fun deleteActivity(activity: Activity) {}
    override suspend fun deleteRecord(record: Record) {}
    override suspend fun saveActivity(activity: Activity) {}
    override fun resetRecordsMonitor() {}
    override fun resetActivitiesMonitor() {}
}
