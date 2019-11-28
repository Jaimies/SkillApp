package com.jdevs.timeo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.getHours

class ActivityViewModel : ViewModel() {
    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime

    var navigator: Navigator? = null

    fun setActivity(activity: TimeoActivity) {
        _name.value = activity.name
        _totalTime.value = activity.totalTime.getHours() + "h"
    }

    interface Navigator {
        fun showRecordDialog()
        fun navigateToDetails()
    }
}
