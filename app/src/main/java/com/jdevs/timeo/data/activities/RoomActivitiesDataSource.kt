package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jdevs.timeo.data.db.ActivitiesDao
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.model.Activity
import com.jdevs.timeo.util.PagingConstants.ACTIVITIES_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesDataSource {

    val activities: LiveData<*>?

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)
}

interface ActivitiesLocalDataSource : ActivitiesDataSource {

    suspend fun saveActivity(activity: Activity)
}

@Singleton
class RoomActivitiesDataSource @Inject constructor(
    private val activitiesDao: ActivitiesDao
) : ActivitiesLocalDataSource {

    override val activities by lazy {

        activitiesDao.getActivities().toLivePagedList(ACTIVITIES_PAGE_SIZE)
    }

    override fun getActivityById(id: Int, documentId: String) =
        Transformations.map(activitiesDao.getActivity(id)) {
            it.mapToDomain()
        }

    override suspend fun addActivity(activity: Activity) =
        activitiesDao.insert(activity.toDB())

    override suspend fun saveActivity(activity: Activity) =
        activitiesDao.update(activity.toDB())

    override suspend fun deleteActivity(activity: Activity) =
        activitiesDao.delete(activity.toDB())
}
