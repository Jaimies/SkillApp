package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.getDaysSinceDate
import com.jdevs.timeo.util.minsToHours

class ActivityDetailsViewModel : ViewModel() {
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

        val daysCount = getDaysSinceDate(activity.timestamp)
        val avgDailyMins = activity.totalTime / (daysCount + 1)

        _avgDailyTime.value = minsToHours(avgDailyMins) + "h"
        _lastWeekTime.value = "42h"
    }
}
