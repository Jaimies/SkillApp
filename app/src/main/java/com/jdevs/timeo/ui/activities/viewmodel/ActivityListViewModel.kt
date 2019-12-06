package com.jdevs.timeo.ui.activities.viewmodel

import com.jdevs.timeo.api.livedata.ActivityListLiveData
import com.jdevs.timeo.api.repository.firestore.ActivitiesRepository
import com.jdevs.timeo.common.viewmodel.ItemListViewModel
import com.jdevs.timeo.data.TimeoActivity

class ActivityListViewModel : ItemListViewModel() {
    var navigator: Navigator? = null
    val activitiesLiveData get() = repository.getLiveData() as ActivityListLiveData?

    private lateinit var repository: ActivitiesRepository

    override fun onFragmentDestroyed() {
        navigator = null
    }

    override fun setupRepository() {
        repository = ActivitiesRepository { navigator?.onLastItemReached() }
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {
        repository.createRecord(activityName, time, activityId)
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
