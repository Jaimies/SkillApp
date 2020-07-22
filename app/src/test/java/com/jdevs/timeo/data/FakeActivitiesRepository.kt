package com.jdevs.timeo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.util.ListDataSource
import com.jdevs.timeo.util.createLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeActivitiesRepository @Inject constructor() : ActivitiesRepository {
    private val activityList = mutableListOf<Activity>()
    override val activities = ListDataSource.Factory(activityList)
    override val topActivities get() = MutableLiveData(activityList.toList())

    override fun getTopLevelActivitiesFromCache(activityIdToExclude: String): LiveData<List<Activity>> {
        return MutableLiveData(activityList.filterNot { it.id == activityIdToExclude })
    }

    override fun getRemoteActivities(fetchNewItems: Boolean) =
        listOf(createLiveData<Operation<Activity>>())

    override suspend fun addActivity(activity: Activity) {
        activityList.add(activity.copy(id = activityList.size.toString()))
        notifyObservers()
    }

    override fun getActivityById(id: String) = MutableLiveData(activityList.single { it.id == id })

    override suspend fun saveActivity(activity: Activity) {
        activityList.replaceAll { if (it.id != activity.id) it else activity }
    }

    override suspend fun deleteActivity(activity: Activity) {
        activityList.remove(activity)
        notifyObservers()
    }

    fun reset() {
        activityList.clear()
        notifyObservers()
    }

    private fun notifyObservers() = activities
}
