package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.ItemDataSource
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.util.createLiveData
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeActivitiesRepository @Inject constructor() : ActivitiesRepository {

    private val activityList = mutableListOf<Activity>()
    override val activities = ItemDataSource.Factory(activityList)

    override fun getRemoteActivities(fetchNewItems: Boolean) =
        listOf(createLiveData<Operation<Activity>>())

    override suspend fun addActivity(name: String) {

        activityList.add(Activity(activityList.size.toString(), name, 0, 0, OffsetDateTime.now()))
        notifyObservers()
    }

    override val topActivities get() = MutableLiveData(activityList.toList())

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
