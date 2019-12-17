package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.RemoteRepository

class ActivityListViewModel : ListViewModel() {

    var navigator: Navigator? = null
    val liveData get() = RemoteRepository.activitiesLiveData

    init {

        RemoteRepository.setupActivitiesSource { navigator?.onLastItemReached() }
    }

    override fun onFragmentDestroyed() {

        navigator = null
    }

    override fun hideLoader() {

        super.hideLoader()
        navigator?.onItemsLoaded()
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {

        RemoteRepository.addRecord(activityName, time, activityId)
    }

    interface Navigator : ListViewModel.Navigator {

        fun createActivity()
        fun onItemsLoaded()
    }
}
