package com.jdevs.timeo.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
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

    override val activitiesLiveData by lazy {

        getLivePagedList(ACTIVITIES_PAGE_SIZE, activitiesDao.getActivities())
    }

    override val recordsLiveData by lazy {

        getLivePagedList(RECORDS_PAGE_SIZE, recordsDao.getRecords())
    }

    override fun getActivityById(id: Int, documentId: String) = activitiesDao.getActivity(id)

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

    override fun reset() {}
    override fun resetRecordsMonitor() {}

    private fun <T> getLivePagedList(
        pageSize: Int,
        factory: DataSource.Factory<Int, T>
    ): LiveData<PagedList<T>> {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        return LivePagedListBuilder(factory, pagedListConfig).build()
    }
}
