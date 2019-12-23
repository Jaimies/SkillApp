package com.jdevs.timeo.data.source.local

import androidx.lifecycle.Transformations
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.data.source.remote.ItemsLiveData

class LocalDataSource(
    private val activitiesDao: ActivitiesDao,
    private val recordsDao: RecordsDao
) : TimeoDataSource {

    override val activities = activitiesDao.getActivities()

    override val records = Transformations.map(recordsDao.getRecords()) { list ->

        return@map list.map { recordAndActivity ->

            recordAndActivity.record.also {
                it.name = recordAndActivity.activity.name
            }
        }
    }

    override val activitiesLiveData: ItemsLiveData? = null
    override val recordsLiveData: ItemsLiveData? = null

    override suspend fun addActivity(activity: Activity) = activitiesDao.insert(activity)

    override suspend fun saveActivity(activity: Activity) = activitiesDao.update(activity)

    override suspend fun deleteActivity(activity: Activity) = activitiesDao.delete(activity)

    override suspend fun addRecord(record: Record) {

        recordsDao.insert(record)
        activitiesDao.increaseTime(record.activityId, record.time)
    }

    override suspend fun deleteRecord(record: Record) {

        recordsDao.delete(record)
        activitiesDao.increaseTime(record.activityId, -record.time)
    }
}
