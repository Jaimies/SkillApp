package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.Activity
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesDataSource {

    fun getTopActivities(): LiveData<List<Activity>>

    fun getActivityById(id: String): LiveData<Activity>

    suspend fun addActivity(name: String)

    suspend fun saveActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)
}

interface ActivitiesLocalDataSource : ActivitiesDataSource {

    val activities: DataSource.Factory<Int, Activity>
}

@Singleton
class RoomActivitiesDataSource @Inject constructor(private val activitiesDao: ActivitiesDao) :
    ActivitiesLocalDataSource {

    override val activities by lazy { activitiesDao.getActivities().map(DBActivity::mapToDomain) }

    override fun getTopActivities() =
        map(activitiesDao.getTopActivities()) { it.map(DBActivity::mapToDomain) }

    override fun getActivityById(id: String) =
        map(activitiesDao.getActivity(id.toInt()), DBActivity::mapToDomain)

    override suspend fun addActivity(name: String) = activitiesDao.insert(DBActivity(name = name))

    override suspend fun saveActivity(activity: Activity) =
        activitiesDao.update(activity.mapToDB())

    override suspend fun deleteActivity(activity: Activity) =
        activitiesDao.delete(activity.mapToDB())
}
