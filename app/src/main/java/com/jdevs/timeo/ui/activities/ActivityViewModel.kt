package com.jdevs.timeo.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.toHours

class ActivityViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val totalTime: LiveData<String> get() = _totalTime
    val navigateToDetails = SingleLiveEvent<Any>()
    val showRecordDialog = SingleLiveEvent<Any>()

    private val _name = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    fun setActivity(activity: Activity) {

        _name.value = activity.name
        _totalTime.value = activity.totalTime.toHours() + "h"
    }

    fun navigateToDetails() = navigateToDetails.call()
    fun showRecordDialog() = showRecordDialog.call()
}
