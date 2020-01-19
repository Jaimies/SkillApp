package com.jdevs.timeo.ui.activities

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.getNextMilestone
import com.jdevs.timeo.util.time.getPrevMilestone
import com.jdevs.timeo.util.time.getProgress
import com.jdevs.timeo.util.time.toHours

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
    open fun setActivity(activity: Activity) {

        _name.value = activity.name
        _totalTime.value = activity.totalTime.toHours() + "h"
        _gradientPercentage.value = activity.totalTime.getProgress()
        _prevMilestone.value = activity.totalTime.getPrevMilestone().toHours() + "h"
        _nextMilestone.value = activity.totalTime.getNextMilestone().toHours() + "h"
    }
}

class ActivityViewModel : ActivityDataViewModel() {

    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()
    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
