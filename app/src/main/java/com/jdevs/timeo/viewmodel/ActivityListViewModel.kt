package com.jdevs.timeo.viewmodel

import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.livedata.ActivityListLiveData
import com.jdevs.timeo.repository.FirestoreActivityListRepository
import com.jdevs.timeo.viewmodel.common.ItemListViewModel

class ActivityListViewModel : ItemListViewModel() {

    var navigator: Navigator? = null

    private val activitiesListRepository =
        FirestoreActivityListRepository { navigator?.onLastItemReached() }

    val activityListLiveData get() = activitiesListRepository.getLiveData() as ActivityListLiveData?

    override fun onFragmentDestroyed() {
        navigator = null
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {
        activitiesListRepository.createRecord(activityName, time, activityId)
    }

    interface Repository {
        fun createRecord(activityName: String, time: Long, activityId: String)
        fun updateActivity(activity: TimeoActivity, activityId: String)
        fun createActivity(activity: TimeoActivity)
        fun deleteActivity(activityId: String)
    }

    interface Navigator : ItemListViewModel.Navigator {
        fun createActivity()
    }
}
