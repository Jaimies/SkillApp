package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.db.ActivitiesDao
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.PagingConstants.ACTIVITIES_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesDataSource {

    val activities: LiveData<*>?

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun saveActivity(activity: Activity): WriteBatch?

    suspend fun deleteActivity(activity: Activity)
}

@Singleton
class ActivitiesLocalDataSource @Inject constructor(
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
