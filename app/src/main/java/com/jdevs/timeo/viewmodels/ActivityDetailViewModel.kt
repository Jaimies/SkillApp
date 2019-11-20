package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.getAvgDailyHours
import com.jdevs.timeo.util.minsToHours

class ActivityDetailViewModel : ViewModel() {
    private val _name = MutableLiveData("")
    private val _avgDailyTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val name: LiveData<String> get() = _name
    val avgDailyTime: LiveData<String> get() = _avgDailyTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val totalTime: LiveData<String> get() = _totalTime

    fun setActivity(activity: TimeoActivity) {
        _name.value = activity.name
        _totalTime.value = minsToHours(activity.totalTime) + "h"
        _avgDailyTime.value = getAvgDailyHours(activity.timestamp, activity.totalTime) + "h"
        _lastWeekTime.value = "42h"
    }
}
