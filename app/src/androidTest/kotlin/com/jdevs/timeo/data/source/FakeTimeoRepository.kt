package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.jdevs.timeo.ItemDataSource
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeTimeoRepository @Inject constructor() : TimeoRepository {

    private val activitiesList = mutableListOf<Activity>()
    private val recordsList = mutableListOf<Record>()

    override val activities = MutableLiveData(activitiesList.asPagedList())
    override val records = MutableLiveData(recordsList.asPagedList())

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        val activity = activitiesList.singleOrNull { it.id == id || it.documentId == documentId }
            ?: Activity()

        return MutableLiveData(activity)
    }

    override suspend fun addActivity(activity: Activity) {

        activitiesList.add(activity)
        activities.postValue(activitiesList.asPagedList())
    }

    override suspend fun addRecord(record: Record) {

        recordsList.add(record)
        records.postValue(recordsList.asPagedList())
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesList.remove(activity)
        activities.postValue(activitiesList.asPagedList())
    }

    override suspend fun deleteRecord(record: Record) {

        recordsList.remove(record)
        records.postValue(recordsList.asPagedList())
    }

    override suspend fun saveActivity(activity: Activity) {

        val index = activitiesList.indexOfFirst { it.id == activity.id }
        activitiesList[index] = activity
        activities.postValue(activitiesList.asPagedList())
    }

    fun reset() {

        activitiesList.clear()
        recordsList.clear()
        activities.postValue(activitiesList.asPagedList())
        records.postValue(recordsList.asPagedList())
    }

    override fun resetRecordsMonitor() {}
    override fun resetActivitiesMonitor() {}

    private fun <T> List<T>.asPagedList(): PagedList<T> {

        return PagedList.Builder<Int, T>(
            ItemDataSource(this), pagedListConfig
        )
            .setFetchExecutor { runBlocking { it.run() } }
            .setNotifyExecutor { runBlocking { it.run() } }
            .build()
    }

    companion object {

        private const val PAGE_SIZE = 50

        private val pagedListConfig = PagedList.Config.Builder()
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()
    }
}
