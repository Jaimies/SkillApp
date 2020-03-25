package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.WeekStats
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

    val dayStats = getStats.dayStats.map {
        it.map(DayStats::toChartItem)
            .toChartData(OffsetDateTime::minusDays, { daysSinceEpoch }, WeekDayFormatter())
    }

    val weekStats = getStats.weekStats.map {
        it.map(WeekStats::toChartItem)
            .toChartData(OffsetDateTime::minusWeeks, { weeksSinceEpoch }, YearWeekFormatter())
    }

    val monthStats = getStats.monthStats.map {
        it.map(MonthStats::toChartItem)
            .toChartData(OffsetDateTime::minusMonths, { monthSinceEpoch }, YearMonthFormatter())
    }

    val showRecordDialog = SingleLiveEvent<Any>()
    val state: LiveData<ActivityDetailState> get() = _state
    private val _state = MutableLiveData<ActivityDetailState>()
    lateinit var activity: LiveData<ActivityItem>

    fun setupActivityLiveData(activity: ActivityItem) {

        this.activity = getActivityById(activity.id).map(Activity::mapToPresentation)
        setData(activity)
    }

    fun addRecord(activity: ActivityItem, time: Int) = launchCoroutine {

        val record = Record(name = activity.name, time = time, activityId = activity.id)
        addRecord(record)
    }

    fun setData(activity: ActivityItem) {
        _state.value = ActivityDetailState(activity)
    }

    fun showRecordDialog() = showRecordDialog.call()

    class ActivityDetailState(activity: ActivityItem) : ActivityState(activity) {

        val avgWeekTime = getAvgWeekHours(activity.totalTime, activity.creationDate) + "h"
        val lastWeekTime = getFriendlyHours(activity.lastWeekTime) + "h"
        val daysSpent = activity.creationDate.getDaysSpentSince().toString()
    }
}
