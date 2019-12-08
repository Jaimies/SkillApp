package com.jdevs.timeo.ui.activities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.getHours

class ActivityViewModel : ViewModel() {

    var navigator: Navigator? = null
    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime

    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    fun setActivity(activity: TimeoActivity) {

        _name.value = activity.name
        _totalTime.value = activity.totalTime.getHours() + "h"
    }

    interface Navigator {

        fun showRecordDialog()
        fun navigateToDetails()
    }
}
