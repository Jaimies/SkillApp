package com.jdevs.timeo.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.util.PagingConstants.ACTIVITIES_PAGE_SIZE
import com.jdevs.timeo.util.PagingConstants.RECORDS_PAGE_SIZE

@Suppress("EmptyFunctionBlock")
class LocalDataSource(
    private val activitiesDao: ActivitiesDao,
    private val recordsDao: RecordsDao
) : TimeoDataSource {

    override val activitiesLiveData: LiveData<PagedList<Activity>>
    override val recordsLiveData: LiveData<PagedList<Record>>

    init {

        val activitiesDataSourceFactory = activitiesDao.getActivities()
        activitiesLiveData =
            LivePagedListBuilder(activitiesDataSourceFactory, ACTIVITIES_PAGE_SIZE).build()

        val recordsDataSourceFactory = recordsDao.getRecords()
        recordsLiveData = LivePagedListBuilder(recordsDataSourceFactory, RECORDS_PAGE_SIZE).build()
    }

    override suspend fun addActivity(activity: Activity) = activitiesDao.insert(activity)

    override suspend fun saveActivity(activity: Activity) = activitiesDao.update(activity)

    override suspend fun deleteActivity(activity: Activity) = activitiesDao.delete(activity)

    override suspend fun addRecord(record: Record) {

        recordsDao.insert(record)
        activitiesDao.increaseTime(record.roomActivityId, record.time)
    }

    override suspend fun deleteRecord(record: Record) {

        recordsDao.delete(record)
        activitiesDao.increaseTime(record.roomActivityId, -record.time)
    }

    override fun resetActivitiesMonitor() {}
    override fun resetRecordsMonitor() {}
}
