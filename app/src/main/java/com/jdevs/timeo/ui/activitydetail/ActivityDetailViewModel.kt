package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.domain.usecase.activities.GetActivityByIdUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.StatsType.DAY
import com.jdevs.timeo.model.StatsType.WEEK
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.time.getDaysSinceEpoch
import com.jdevs.timeo.shared.time.getMonthSinceEpoch
import com.jdevs.timeo.shared.time.getWeeksSinceEpoch
import com.jdevs.timeo.ui.activities.ActivityDataViewModel
import com.jdevs.timeo.ui.common.WeekDayFormatter
import com.jdevs.timeo.ui.common.YearMonthFormatter
import com.jdevs.timeo.ui.common.YearWeekFormatter
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getHours
import com.jdevs.timeo.util.toChartData
import com.jdevs.timeo.util.toChartItem
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityDetailViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val addRecord: AddRecordUseCase,
    getStats: GetStatsUseCase
) : ActivityDataViewModel() {

    val avgWeekTime: LiveData<String> get() = _avgWeekTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val daysSpent: LiveData<String> get() = _daysSpent

    val chartType: LiveData<Int> get() = _chartType
    private val _chartType = MutableLiveData(DAY)

    val stats = switchMap(_chartType) {
        when (_chartType.value) {

            DAY -> map(getStats.dayStats) {
                it.map(DayStats::toChartItem).toChartData(
                    OffsetDateTime::minusDays, OffsetDateTime::getDaysSinceEpoch, WeekDayFormatter()
                )
            }

            WEEK -> map(getStats.weekStats) {
                it.map(WeekStats::toChartItem).toChartData(
                    OffsetDateTime::minusWeeks,
                    OffsetDateTime::getWeeksSinceEpoch, YearWeekFormatter()
                )
            }

            else -> map(getStats.monthStats) {
                it.map(MonthStats::toChartItem).toChartData(
                    OffsetDateTime::minusMonths,
                    OffsetDateTime::getMonthSinceEpoch, YearMonthFormatter()
                )
            }
        }
    }
    private val _avgWeekTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")

    private val _daysSpent = MutableLiveData("")
    val showRecordDialog = SingleLiveEvent<Any>()

    lateinit var activity: LiveData<ActivityItem>

    fun setChartType(type: Int) {

        _chartType.value = type
    }

    override fun setActivity(activity: ActivityItem) {

        super.setActivity(activity)
        _avgWeekTime.value = getAvgWeekHours(activity.totalTime, activity.creationDate) + "h"
        _lastWeekTime.value = getHours(activity.lastWeekTime) + "h"
        _daysSpent.value = activity.creationDate.getDaysSpentSince().toString()
    }

    fun setupActivityLiveData(activity: ActivityItem) {

        this.activity = map(getActivityById(activity.id), Activity::mapToPresentation)
        setActivity(activity)
    }

    fun addRecord(activity: ActivityItem, time: Int) = viewModelScope.launch {

        val record = Record(name = activity.name, time = time, activityId = activity.id)

        addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()
}
