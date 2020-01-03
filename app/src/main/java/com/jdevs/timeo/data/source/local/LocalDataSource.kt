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
import com.jdevs.timeo.util.PagingConstants.STATS_PAGE_SIZE

@Suppress("EmptyFunctionBlock")
class LocalDataSource(
    private val activitiesDao: ActivitiesDao,
    private val recordsDao: RecordsDao
) : TimeoDataSource {

    override val activities by lazy {

        activitiesDao.getActivities().toLivePagedList(ACTIVITIES_PAGE_SIZE)
    }

    override val records by lazy {

        recordsDao.getRecords().toLivePagedList(RECORDS_PAGE_SIZE)
    }

    override val dayStats by lazy {

        recordsDao.getDayStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override val weekStats by lazy {

        recordsDao.getWeekStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override val monthStats by lazy {

        recordsDao.getMonthStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override fun getActivityById(id: Int, documentId: String) = activitiesDao.getActivity(id)

    override suspend fun addActivity(activity: Activity) = activitiesDao.insert(activity)

    override suspend fun saveActivity(activity: Activity) = activitiesDao.update(activity)

    override suspend fun deleteActivity(activity: Activity) = activitiesDao.delete(activity)

    override suspend fun addRecord(record: Record) {

        recordsDao.insertRecord(record)
        activitiesDao.increaseTime(record.roomActivityId, record.time)
    }

    override suspend fun deleteRecord(record: Record) {

        recordsDao.deleteRecord(record)
        activitiesDao.increaseTime(record.roomActivityId, -record.time)
    }

    override fun resetActivitiesMonitor() {}
    override fun resetRecordsMonitor() {}
    override fun resetStatsMonitor() {}

    private fun <T> DataSource.Factory<Int, T>.toLivePagedList(
        pageSize: Int
    ): LiveData<PagedList<T>> {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        return LivePagedListBuilder(this, pagedListConfig).build()
    }
}
