package com.jdevs.timeo.data.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultActivitiesRepository @Inject constructor(
    private val dataSource: ActivitiesDataSource
) : ActivitiesRepository {

    override val activities get() = dataSource.activities
    override val topActivities get() = dataSource.getTopActivities()

    override fun getParentActivitySuggestions(activityId: Int) =
        dataSource.getParentActivitySuggestions(activityId)

    override fun getActivityById(id: Int) = dataSource.getActivityById(id)

    override suspend fun addActivity(activity: Activity) =
        dataSource.addActivity(activity)

    override suspend fun saveActivity(activity: Activity) =
        dataSource.saveActivity(activity)

    override suspend fun deleteActivity(activity: Activity) =
        dataSource.deleteActivity(activity)
}
