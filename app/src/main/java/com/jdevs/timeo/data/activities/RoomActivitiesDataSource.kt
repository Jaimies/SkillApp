package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.PagingConstants.ACTIVITIES_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesDataSource {

    fun getTopActivities(): LiveData<List<Activity>>

    fun getActivityById(id: Int, documentId: String): LiveData<Activity>

    suspend fun addActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)
}

interface ActivitiesLocalDataSource : ActivitiesDataSource {

    val activities: LiveData<PagedList<Activity>>

    suspend fun saveActivity(activity: Activity)
}

@Singleton
class RoomActivitiesDataSource @Inject constructor(
    private val activitiesDao: ActivitiesDao,
    private val mapper: DBActivityMapper,
    private val domainMapper: DBDomainActivityMapper
) : ActivitiesLocalDataSource {

    override val activities by lazy {

        activitiesDao.getActivities().toLivePagedList(ACTIVITIES_PAGE_SIZE, domainMapper)
    }

    override fun getTopActivities() =
        Transformations.map(activitiesDao.getTopActivities()) { it.map(domainMapper::map) }

    override fun getActivityById(id: Int, documentId: String) =
        Transformations.map(activitiesDao.getActivity(id)) { domainMapper.map(it) }

    override suspend fun addActivity(activity: Activity) =
        activitiesDao.insert(mapper.map(activity))

    override suspend fun saveActivity(activity: Activity) =
        activitiesDao.update(mapper.map(activity))

    override suspend fun deleteActivity(activity: Activity) =
        activitiesDao.delete(mapper.map(activity))
}
