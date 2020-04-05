package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.ActivityDayStats
import com.jdevs.timeo.domain.model.ActivityMonthStats
import com.jdevs.timeo.domain.model.ActivityWeekStats
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivityByIdUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.monthSinceEpoch
import com.jdevs.timeo.shared.util.weeksSinceEpoch
import com.jdevs.timeo.ui.activities.ActivityState
import com.jdevs.timeo.ui.common.WeekDayFormatter
import com.jdevs.timeo.ui.common.YearMonthFormatter
import com.jdevs.timeo.ui.common.YearWeekFormatter
import com.jdevs.timeo.util.charts.toChartData
import com.jdevs.timeo.util.charts.toChartItem
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getFriendlyHours
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ActivityDetailViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val addRecord: AddRecordUseCase,
    getStats: GetStatsUseCase
) : ViewModel() {

    val dayStats by lazy {
        getStats.getDayStats(activityId).map {
            it.map(ActivityDayStats::toChartItem)
                .toChartData(OffsetDateTime::minusDays, { daysSinceEpoch }, WeekDayFormatter())
        }
    }

    val weekStats by lazy {
        getStats.getWeekStats(activityId).map {
            it.map(ActivityWeekStats::toChartItem)
                .toChartData(OffsetDateTime::minusWeeks, { weeksSinceEpoch }, YearWeekFormatter())
        }
    }

    val monthStats by lazy {
        getStats.getMonthStats(activityId).map {
            it.map(ActivityMonthStats::toChartItem)
                .toChartData(OffsetDateTime::minusMonths, { monthSinceEpoch }, YearMonthFormatter())
        }
    }

    val showRecordDialog = SingleLiveEvent<Any>()
    val showParentRecordDialog = SingleLiveEvent<Any>()
    val navigateToParentActivity = SingleLiveEvent<Any>()
    val state: LiveData<ActivityDetailState> get() = _state
    private val _state = MutableLiveData<ActivityDetailState>()
    lateinit var activity: LiveData<ActivityItem>
    private var activityId = ""

    fun setupActivityLiveData(id: String) {
        activityId = id
        this.activity = getActivityById(id).map(Activity::mapToPresentation)
    }

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
}
