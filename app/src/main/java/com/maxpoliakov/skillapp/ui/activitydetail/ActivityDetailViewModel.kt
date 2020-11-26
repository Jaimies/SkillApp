package com.maxpoliakov.skillapp.ui.activitydetail

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.usecase.activities.GetActivityByIdUseCase
import com.maxpoliakov.skillapp.domain.usecase.records.AddRecordUseCase
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.ui.activities.ActivityState
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel
import com.maxpoliakov.skillapp.util.lifecycle.launchCoroutine
import com.maxpoliakov.skillapp.util.time.getAvgWeekHours
import com.maxpoliakov.skillapp.util.time.getDaysSpentSince
import com.maxpoliakov.skillapp.util.time.getFriendlyHours
import kotlinx.coroutines.flow.map
import java.time.Duration
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

    val activity = getActivityById.run(activityId)
        .map { it.mapToPresentation() }
        .asLiveData()

    val state = activity.map { ActivityDetailState(it) }

    fun addRecord(activityId: Int, activityName: String, time: Duration) {
        launchCoroutine {
            val record = Record(activityName, activityId, time)
            addRecord.run(record)
        }
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
            return ActivityDetailViewModel(
                addRecord,
                getActivityById,
                getStats,
                activityId
            )
        }
    }
}
