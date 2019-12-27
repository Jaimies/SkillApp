package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.getAvgDailyHours
import com.jdevs.timeo.util.getHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityDetailViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ViewModel() {

    val name: LiveData<String> get() = _name
    val avgDailyTime: LiveData<String> get() = _avgDailyTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val totalTime: LiveData<String> get() = _totalTime
    lateinit var activity: LiveData<Activity>

    val showRecordDialog = SingleLiveEvent<Any>()
    private val _name = MutableLiveData("")
    private val _avgDailyTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _totalTime = MutableLiveData("")

    fun setActivity(activity: Activity) {

        _name.value = activity.name
        _totalTime.value = activity.totalTime.getHours() + "h"
        _avgDailyTime.value = activity.totalTime.getAvgDailyHours(activity.timestamp) + "h"
        _lastWeekTime.value = "42h"
    }

    fun setupActivityLiveData(activity: Activity) {

        this.activity = repository.getActivityById(activity.id, activity.documentId)
    }

    fun addRecord(activity: Activity, time: Long) = viewModelScope.launch {

        val record = Record(
            name = activity.name,
            time = time,
            activityId = activity.documentId,
            roomActivityId = activity.id
        )

        repository.addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
