package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.activities.GetActivityByIdUseCase
import com.jdevs.timeo.domain.records.AddRecordUseCase
import com.jdevs.timeo.model.Activity
import com.jdevs.timeo.model.Record
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgDailyHours
import com.jdevs.timeo.util.time.toHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityDetailViewModel @Inject constructor(
    private val getActivityByIdUseCase: GetActivityByIdUseCase,
    private val addRecordUseCase: AddRecordUseCase
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
        _totalTime.value = activity.totalTime.toHours() + "h"
        _avgDailyTime.value = activity.totalTime.getAvgDailyHours(activity.creationDate) + "h"
        _lastWeekTime.value = activity.lastWeekTime.toHours() + "h"
    }

    fun setupActivityLiveData(activity: Activity) {

        this.activity = getActivityByIdUseCase.getActivityById(activity.id, activity.documentId)
    }

    fun addRecord(activity: Activity, time: Long) = viewModelScope.launch {

        val record = Record(
            name = activity.name,
            time = time,
            activityId = activity.documentId,
            roomActivityId = activity.id
        )

        addRecordUseCase.addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
