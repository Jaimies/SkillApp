package com.jdevs.timeo.ui.activities.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.remote.RemoteRepository
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch

class ActivityListViewModel(application: Application) : ListViewModel() {

    override val liveData get() = RemoteRepository.activitiesLiveData
    val navigateToAddEdit = SingleLiveEvent<Any>()
    val activities: LiveData<List<Activity>>
    private val repository: ActivitiesRepository =
        (application as TimeoApplication).activitiesRepository

    init {

        activities = repository.getActivities()
        RemoteRepository.setupActivitiesSource { onLastItemReached.call() }
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {

        RemoteRepository.addRecord(activityName, time, activityId)
    }

    fun insert(activity: Activity) = viewModelScope.launch {
        repository.addActivity(activity)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ActivityListViewModel(application) as T
        }
    }
}
