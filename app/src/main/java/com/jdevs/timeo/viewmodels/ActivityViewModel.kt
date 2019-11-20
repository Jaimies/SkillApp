package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.navigators.ActivityNavigator
import com.jdevs.timeo.util.minsToHours

class ActivityViewModel : ViewModel() {
    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime

    var navigator: ActivityNavigator? = null

    fun setActivity(activity: TimeoActivity) {
        _name.value = activity.name
        _totalTime.value = minsToHours(activity.totalTime) + "h"
    }
}
