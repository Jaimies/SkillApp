package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.add
import com.jdevs.timeo.clear
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.indexOfFirst
import com.jdevs.timeo.remove
import com.jdevs.timeo.set

class FakeDataSource(
    activityList: List<Activity> = emptyList(),
    recordList: List<Record> = emptyList()
) : TimeoDataSource {

    override val activitiesLiveData: LiveData<*>? get() = _activities
    override val recordsLiveData: LiveData<*>? get() = _records

    private val _activities = MutableLiveData(activityList.toMutableList())
    private val _records = MutableLiveData(recordList.toMutableList())

    override suspend fun addActivity(activity: Activity) {

        _activities.add(activity)
    }

    override suspend fun addRecord(record: Record) {

        _records.add(record)
    }

    override suspend fun deleteActivity(activity: Activity) {

        _activities.remove(activity)
    }

    override suspend fun deleteRecord(record: Record) {

        _records.remove(record)
    }

    override suspend fun saveActivity(activity: Activity) {

        val index = _activities.indexOfFirst { it.id == activity.id } ?: return
        _activities[index] = activity
    }

    override fun reset() {

        _activities.clear()
        _records.clear()
    }

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        val activity = _activities.value?.single { it.id == id }!!
        return MutableLiveData(activity)
    }

    override fun resetRecordsMonitor() {}
}
