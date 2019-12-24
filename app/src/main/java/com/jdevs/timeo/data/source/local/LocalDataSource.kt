package com.jdevs.timeo.data.source.local

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoDataSource

class LocalDataSource(
    private val activitiesDao: ActivitiesDao,
    private val recordsDao: RecordsDao
) : TimeoDataSource {

    override val activitiesLiveData = activitiesDao.getActivities()
    override val recordsLiveData = recordsDao.getRecords()

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
}