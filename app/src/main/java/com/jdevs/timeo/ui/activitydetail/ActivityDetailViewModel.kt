package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivityByIdUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.ui.activities.ActivityDataViewModel
import com.jdevs.timeo.util.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgDailyHours
import com.jdevs.timeo.util.time.toHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityDetailViewModel @Inject constructor(
    private val getActivityByIdUseCase: GetActivityByIdUseCase,
    private val addRecordUseCase: AddRecordUseCase
) : ActivityDataViewModel() {

    val avgDailyTime: LiveData<String> get() = _avgDailyTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    private val _avgDailyTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")

    val showRecordDialog = SingleLiveEvent<Any>()
    lateinit var activity: LiveData<Activity>

    override fun setActivity(activity: Activity) {

        super.setActivity(activity)
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
