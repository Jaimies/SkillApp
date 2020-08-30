package com.jdevs.timeo.ui.activitydetail

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
    private val addRecord: AddRecordUseCase,
    getActivityById: GetActivityByIdUseCase,
    getStats: GetStatsUseCase,
    activityId: Int
) : StatsViewModel(getStats, activityId) {

    val showRecordDialog = SingleLiveEvent<Any>()
    val showParentRecordDialog = SingleLiveEvent<Any>()
    val navigateToParentActivity = SingleLiveEvent<Any>()

    val activity = getActivityById(activityId).map(Activity::mapToPresentation)
    val state = activity.map { ActivityDetailState(it) }

    fun addRecord(activityId: Int, activityName: String, time: Int) = launchCoroutine {
        val record = Record(name = activityName, time = time, activityId = activityId)
        addRecord(record)
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
        private val addRecord: AddRecordUseCase,
        private val getActivityById: GetActivityByIdUseCase,
        private val getStats: GetStatsUseCase
    ) {
        fun create(activityId: Int): ActivityDetailViewModel {
            return ActivityDetailViewModel(addRecord, getActivityById, getStats, activityId)
        }
    }
}
