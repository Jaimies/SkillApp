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
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getHours
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityDetailViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val addRecord: AddRecordUseCase
) : ActivityDataViewModel() {

    val avgWeekTime: LiveData<String> get() = _avgWeekTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val daysSpent: LiveData<String> get() = _daysSpent
    private val _avgWeekTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _daysSpent = MutableLiveData("")

    val showRecordDialog = SingleLiveEvent<Any>()
    lateinit var activity: LiveData<Activity>

    override fun setActivity(activity: Activity) {

        super.setActivity(activity)
        _avgWeekTime.value = getAvgWeekHours(activity.totalTime, activity.creationDate) + "h"
        _lastWeekTime.value = getHours(activity.lastWeekTime) + "h"
        _daysSpent.value = activity.creationDate.getDaysSpentSince().toString()
    }

    fun setupActivityLiveData(activity: Activity) {

        this.activity = getActivityById(activity.id, activity.documentId)
        setActivity(activity)
    }

    fun addRecord(activity: Activity, time: Long) = viewModelScope.launch {

        val record = Record(
            name = activity.name,
            time = time,
            activityId = activity.documentId,
            roomActivityId = activity.id
        )

        addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
