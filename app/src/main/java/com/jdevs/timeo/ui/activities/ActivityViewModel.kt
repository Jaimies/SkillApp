package com.jdevs.timeo.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.util.time.getFriendlyHours

open class ActivityState(activity: ActivityItem) {

    val name = activity.name
    val totalTime = getFriendlyHours(activity.totalTime)

    val hasSubActivities = activity.parentActivityId == "" && activity.subActivities.isNotEmpty()
    val subActivitiesCount = activity.subActivities.size
    val hasParentActivity = activity.parentActivityId != ""
    val parentActivityName = activity.parentActivityName
}

class ActivityViewModel {

    val state: LiveData<ActivityState> get() = _state
    private val _state = MutableLiveData<ActivityState>()

    fun setData(activity: ActivityItem) {

        _state.value = ActivityState(activity)
    }

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
