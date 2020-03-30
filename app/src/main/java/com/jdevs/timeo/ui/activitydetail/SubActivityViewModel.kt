package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityMinimalItem
import com.jdevs.timeo.util.time.getFriendlyHours

open class BaseActivityState(activity: ActivityMinimalItem) {

    val name = activity.name
    val totalTime = getFriendlyHours(activity.totalTime)
}

class SubActivityViewModel {

    val state get() = _state as LiveData<BaseActivityState>
    private val _state = MutableLiveData<BaseActivityState>()

    fun setData(activity: ActivityMinimalItem) {
        _state.value = BaseActivityState(activity)
    }

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
