package com.jdevs.timeo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.livedata.ActivityListLiveData
import com.jdevs.timeo.repository.FirestoreActivitiesListRepository

class ActivitiesListViewModel : ViewModel() {

    private val _isEmpty = MutableLiveData(true)
    private val _isLoaded = MutableLiveData(false)

    val isEmpty: LiveData<Boolean> get() = _isEmpty
    val isLoaded: LiveData<Boolean> get() = _isLoaded

    var navigator: Navigator? = null

    private val activitiesListRepository =
        FirestoreActivitiesListRepository()

    val activityListLiveData get() = activitiesListRepository.getActivitiesListLiveData()

    fun onLoaded() {
        _isLoaded.value = true
    }

    fun setLength(length: Int) {
        if (length > 0) {
            _isEmpty.value = false
        }
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {
        activitiesListRepository.createRecord(activityName, time, activityId)
    }

    interface ActivitiesListRepository {
        fun getActivitiesListLiveData(): ActivityListLiveData?
        fun createRecord(activityName: String, time: Long, activityId: String)
        fun updateActivity(activity: TimeoActivity, activityId: String)
        fun createActivity(activity: TimeoActivity)
        fun deleteActivity(activityId: String)
    }

    interface Navigator {
        fun createActivity()
    }
}
