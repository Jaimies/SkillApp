package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivityByIdUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.activities.ActivityState
import com.jdevs.timeo.ui.stats.StatsViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getFriendlyHours
import javax.inject.Inject

class ActivityDetailViewModel(
    private val getActivityById: GetActivityByIdUseCase,
    private val addRecord: AddRecordUseCase,
    getStats: GetStatsUseCase,
    activityId: String
) : StatsViewModel(getStats, activityId) {

    val showRecordDialog = SingleLiveEvent<Any>()
    val showParentRecordDialog = SingleLiveEvent<Any>()
    val navigateToParentActivity = SingleLiveEvent<Any>()

    val state: LiveData<ActivityDetailState> get() = _state
    private val _state = MutableLiveData<ActivityDetailState>()

    val activity = getActivityById(activityId).map(Activity::mapToPresentation)

    fun addRecord(activityId: String, activityName: String, time: Int) = launchCoroutine {
        val record = Record(name = activityName, time = time, activityId = activityId)
        addRecord(record)
    }

    fun setActivity(activity: ActivityItem) {
        _state.value = ActivityDetailState(activity)
    }

    fun showRecordDialog() = showRecordDialog.call()
    fun showParentRecordDialog() = showParentRecordDialog.call()
    fun navigateToParentActivity() = navigateToParentActivity.call()

    class ActivityDetailState(activity: ActivityItem) : ActivityState(activity) {
        val avgWeekTime = getAvgWeekHours(activity.totalTime, activity.creationDate)
        val lastWeekTime = getFriendlyHours(activity.lastWeekTime)
        val daysSpent = activity.creationDate.getDaysSpentSince().toString()
    }

    class Factory @Inject constructor(
        private val getActivityById: GetActivityByIdUseCase,
        private val addRecord: AddRecordUseCase,
        private val getStats: GetStatsUseCase
    ) {
        fun create(activityId: String): ActivityDetailViewModel {
            return ActivityDetailViewModel(getActivityById, addRecord, getStats, activityId)
        }
    }
}
