package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.navigators.ActivityNavigator
import com.jdevs.timeo.util.Time

class ActivityViewModel : ViewModel() {
    private val _name = MutableLiveData("Test")
    private val _totalTime = MutableLiveData(0)

    var navigator: ActivityNavigator? = null

    val name: LiveData<String> get() = _name
    val totalTime: String get() = Time.minsToHours(_totalTime.value!!) + "h"

    fun setActivity(activity: TimeoActivity) {
        _name.value = activity.name
        _totalTime.value = activity.totalTime
    }
}