package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.Transformations
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.RemoteRepository
import com.jdevs.timeo.util.SingleLiveEvent

class ActivityListViewModel : ListViewModel() {

    override val liveData get() = RemoteRepository.activitiesLiveData
    val navigateToAddEdit = SingleLiveEvent<Any>()
    val onLoaded = SingleLiveEvent<Any>()

    init {

        RemoteRepository.setupActivitiesSource { onLastItemReached.call() }

        Transformations.map(isLoading) { isLoading ->

            if (!isLoading) {

                onLoaded.call()
            }
        }
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {

        RemoteRepository.addRecord(activityName, time, activityId)
    }

    fun navigateToAddActivity() = navigateToAddEdit.call()
}
