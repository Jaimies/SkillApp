package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.remote.RemoteRepository
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch

class ActivityListViewModel(private val repository: ActivitiesRepository) : ListViewModel() {

    override val liveData get() = RemoteRepository.activitiesLiveData
    val navigateToAddEdit = SingleLiveEvent<Any>()
    val activities: LiveData<List<Activity>> = repository.getActivities()

    init {

        RemoteRepository.setupActivitiesSource { onLastItemReached.call() }
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {

        RemoteRepository.addRecord(activityName, time, activityId)
    }

    fun insert(activity: Activity) = viewModelScope.launch {
        repository.addActivity(activity)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
