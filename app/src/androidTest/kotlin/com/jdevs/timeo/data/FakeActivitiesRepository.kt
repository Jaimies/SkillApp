package com.jdevs.timeo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.model.Activity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeActivitiesRepository @Inject constructor() : ActivitiesRepository {

    private val activityList = mutableListOf<Activity>()
    override val activities = MutableLiveData(activityList.asPagedList())

    override suspend fun addActivity(activity: Activity) {

        activityList.add(activity)
        notifyObservers()
    }

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        return MutableLiveData(activityList.single { it.documentId == documentId })
    }

    override suspend fun saveActivity(activity: Activity): WriteBatch? {

        activityList.replaceAll { if (it.documentId != activity.documentId) it else activity }
        return null
    }

    override suspend fun deleteActivity(activity: Activity) {

        activityList.remove(activity)
        notifyObservers()
    }

    override suspend fun increaseTime(activityId: String, time: Long, batch: WriteBatch) {

        activityList.replaceAll {
            if (it.documentId != activityId) it else it.copy(totalTime = it.totalTime + time)
        }
    }

    fun reset() {

        activityList.clear()
        notifyObservers()
    }

    override fun resetActivitiesMonitor() {}

    private fun notifyObservers() = activities.postValue(activityList.asPagedList())
}
