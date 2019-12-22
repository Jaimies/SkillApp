package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.data.source.remote.RemoteRepository
import com.jdevs.timeo.util.SingleLiveEvent
import kotlinx.coroutines.launch

class ActivityListViewModel(private val repository: TimeoRepository) : ListViewModel() {

    override val liveData get() = RemoteRepository.activitiesLiveData
    val navigateToAddEdit = SingleLiveEvent<Any>()
    val activities: LiveData<List<Activity>> = repository.getActivities()

    init {

        RemoteRepository.setupActivitiesSource { onLastItemReached.call() }
    }

    fun createRecord(record: Record) = viewModelScope.launch {

        repository.addRecord(record)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
