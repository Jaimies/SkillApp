package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
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
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.StatsType.DAY
import com.jdevs.timeo.model.StatsType.WEEK
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.util.getDaysSinceEpoch
import com.jdevs.timeo.shared.util.getMonthSinceEpoch
import com.jdevs.timeo.shared.util.getWeeksSinceEpoch
import com.jdevs.timeo.ui.activities.ActivityDataHolder
import com.jdevs.timeo.ui.common.WeekDayFormatter
import com.jdevs.timeo.ui.common.YearMonthFormatter
import com.jdevs.timeo.ui.common.YearWeekFormatter
import com.jdevs.timeo.util.charts.toChartData
import com.jdevs.timeo.util.charts.toChartItem
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getFriendlyHours
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityDetailViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val addRecord: AddRecordUseCase,
    getStats: GetStatsUseCase
) : ViewModel() {

    val chartType: LiveData<Int> get() = _chartType
    private val _chartType = MutableLiveData(DAY)

    val stats = switchMap(_chartType) {
        when (_chartType.value) {

            DAY -> getStats.dayStats.map {
                it.map(DayStats::toChartItem).toChartData(
                    OffsetDateTime::minusDays, OffsetDateTime::getDaysSinceEpoch, WeekDayFormatter()
                )
            }

            WEEK -> getStats.weekStats.map {
                it.map(WeekStats::toChartItem).toChartData(
                    OffsetDateTime::minusWeeks,
                    OffsetDateTime::getWeeksSinceEpoch, YearWeekFormatter()
                )
            }

            else -> getStats.monthStats.map {
                it.map(MonthStats::toChartItem).toChartData(
                    OffsetDateTime::minusMonths,
                    OffsetDateTime::getMonthSinceEpoch, YearMonthFormatter()
                )
            }
        }
    }

    val showRecordDialog = SingleLiveEvent<Any>()
    val activityData = ActivityDetailDataHolder()
    lateinit var activity: LiveData<ActivityItem>

    fun setChartType(type: Int) {

        _chartType.value = type
    }

    fun setupActivityLiveData(activity: ActivityItem) {

        this.activity = getActivityById(activity.id).map(Activity::mapToPresentation)
        activityData.setData(activity)
    }

    fun addRecord(activity: ActivityItem, time: Int) = launchCoroutine {

        val record = Record(name = activity.name, time = time, activityId = activity.id)
        addRecord(record)
    }

    fun showRecordDialog() = showRecordDialog.call()

    class ActivityDetailDataHolder : ActivityDataHolder() {

        val avgWeekTime: LiveData<String> get() = _avgWeekTime
        val lastWeekTime: LiveData<String> get() = _lastWeekTime
        val daysSpent: LiveData<String> get() = _daysSpent

        private val _avgWeekTime = MutableLiveData("")
        private val _lastWeekTime = MutableLiveData("")
        private val _daysSpent = MutableLiveData("")

        override fun setData(activity: ActivityItem) {

            super.setData(activity)
            _avgWeekTime.value = getAvgWeekHours(activity.totalTime, activity.creationDate) + "h"
            _lastWeekTime.value = getFriendlyHours(activity.lastWeekTime) + "h"
            _daysSpent.value = activity.creationDate.getDaysSpentSince().toString()
        }
    }
}
