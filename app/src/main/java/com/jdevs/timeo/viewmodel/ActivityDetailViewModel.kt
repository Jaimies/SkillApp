package com.jdevs.timeo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.getAvgDailyHours
import com.jdevs.timeo.util.getHours

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
        _totalTime.value = activity.totalTime.getHours() + "h"
        _avgDailyTime.value = activity.totalTime.getAvgDailyHours(activity.timestamp) + "h"
        _lastWeekTime.value = "42h"
    }
}
