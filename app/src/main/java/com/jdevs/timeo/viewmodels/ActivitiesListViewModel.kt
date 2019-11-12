package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.livedata.ActivitiesListLiveData

class ActivitiesListViewModel : ViewModel() {
    private val activitiesListRepository = FirestoreActivitiesListRepository()

    fun getActivitiesListLiveData(): ActivitiesListLiveData? {
        return activitiesListRepository.getActivitiesListLiveData()
    }

    fun createRecord(activityName: String, workingTime: Int, activityId: String) {
        activitiesListRepository.createRecord(activityName, workingTime, activityId)
    }

    interface ActivitiesListRepository {
        fun getActivitiesListLiveData(): ActivitiesListLiveData?
        fun createRecord(activityName: String, workingTime: Int, activityId: String)
        fun updateActivity(activity: TimeoActivity, activityId: String)
        fun createActivity(activity: TimeoActivity)
        fun deleteActivity(activityId: String)
    }
}
