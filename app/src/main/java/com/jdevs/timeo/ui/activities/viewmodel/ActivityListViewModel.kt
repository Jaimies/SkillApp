package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.common.viewmodel.ItemListViewModel
import com.jdevs.timeo.data.source.RemoteRepository

class ActivityListViewModel : ItemListViewModel() {

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

    interface Navigator : ItemListViewModel.Navigator {

        fun createActivity()
        fun onItemsLoaded()
    }
}
