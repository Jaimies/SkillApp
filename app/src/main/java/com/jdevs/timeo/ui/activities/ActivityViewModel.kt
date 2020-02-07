package com.jdevs.timeo.ui.activities

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.ui.model.ActivityItem
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getHours
import com.jdevs.timeo.util.time.getNextMilestone
import com.jdevs.timeo.util.time.getPrevMilestone
import com.jdevs.timeo.util.time.getProgress

open class ActivityDataViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime
    val gradientPercentage: LiveData<Int> get() = _gradientPercentage
    val prevMilestone: LiveData<String> get() = _prevMilestone
    val nextMilestone: LiveData<String> get() = _nextMilestone
    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")
    private val _gradientPercentage = MutableLiveData<Int>()
    private val _prevMilestone = MutableLiveData("")
    private val _nextMilestone = MutableLiveData("")

    @CallSuper
    open fun setActivity(activity: ActivityItem) {

        _name.value = activity.name
        _totalTime.value = getHours(activity.totalTime) + "h"
        _gradientPercentage.value = getProgress(activity.totalTime)
        _prevMilestone.value = getHours(getPrevMilestone(activity.totalTime)) + "h"
        _nextMilestone.value = getHours(getNextMilestone(activity.totalTime)) + "h"
    }
}

class ActivityViewModel : ActivityDataViewModel() {

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
