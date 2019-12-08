package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.api.livedata.ActivityListLiveData
import com.jdevs.timeo.api.repository.firestore.ActivitiesRepository
import com.jdevs.timeo.common.viewmodel.ItemListViewModel

class ActivityListViewModel : ItemListViewModel() {

    var navigator: Navigator? = null
    val liveData get() = repository.getLiveData() as ActivityListLiveData?

    override val repository = ActivitiesRepository { navigator?.onLastItemReached() }

    override fun onFragmentDestroyed() {

        navigator = null
    }

    override fun hideLoader() {

        super.hideLoader()
        navigator?.onItemsLoaded()
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {

        repository.createRecord(activityName, time, activityId)
    }

    interface Navigator : ItemListViewModel.Navigator {

        fun createActivity()
        fun onItemsLoaded()
    }
}
