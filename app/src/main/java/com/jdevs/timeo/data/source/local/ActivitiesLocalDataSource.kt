package com.jdevs.timeo.data.source.local

import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesDataSource
import com.jdevs.timeo.util.PagingConstants.ACTIVITIES_PAGE_SIZE

class ActivitiesLocalDataSource(
    private val activitiesDao: ActivitiesDao
) : ActivitiesDataSource {

    override val activities by lazy {

        activitiesDao.getActivities().toLivePagedList(ACTIVITIES_PAGE_SIZE)
    }

    override fun getActivityById(id: Int, documentId: String) = activitiesDao.getActivity(id)

    override suspend fun addActivity(activity: Activity) = activitiesDao.insert(activity)

    override suspend fun saveActivity(activity: Activity): WriteBatch? {

        activitiesDao.update(activity)
        return null
    }

    override suspend fun deleteActivity(activity: Activity) = activitiesDao.delete(activity)
}
