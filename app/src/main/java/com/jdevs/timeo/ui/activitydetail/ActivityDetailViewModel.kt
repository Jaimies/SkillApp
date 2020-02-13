package com.jdevs.timeo.ui.activitydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.usecase.activities.GetActivityByIdUseCase
import com.jdevs.timeo.domain.usecase.records.AddRecordUseCase
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.DayStatsItem
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.shared.time.WEEK_DAYS
import com.jdevs.timeo.shared.time.getDaysSinceEpoch
import com.jdevs.timeo.ui.activities.ActivityDataViewModel
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import com.jdevs.timeo.util.time.getAvgWeekHours
import com.jdevs.timeo.util.time.getDaysSpentSince
import com.jdevs.timeo.util.time.getHours
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class ActivityDetailViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val addRecord: AddRecordUseCase,
    getStats: GetStatsUseCase
) : ActivityDataViewModel() {

    val avgWeekTime: LiveData<String> get() = _avgWeekTime
    val lastWeekTime: LiveData<String> get() = _lastWeekTime
    val daysSpent: LiveData<String> get() = _daysSpent
    val stats by lazy { map(getStats.dayStats, ::convertStatsToChartData) }

    private val _avgWeekTime = MutableLiveData("")
    private val _lastWeekTime = MutableLiveData("")
    private val _daysSpent = MutableLiveData("")

    val showRecordDialog = SingleLiveEvent<Any>()
    lateinit var activity: LiveData<ActivityItem>

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

    fun addRecord(activity: ActivityItem, time: Long) = viewModelScope.launch {

        val record = Record(name = activity.name, time = time, activityId = activity.id)

        addRecord(record)
    }

    private fun convertStatsToChartData(stats: List<DayStats>): List<Entry> {

        val result = mutableListOf<DayStatsItem>()

        for (index in 0 until WEEK_DAYS) {

            val daysSince = OffsetDateTime.now().minusDays(index.toLong()).getDaysSinceEpoch()

            if (stats.count { it.day == daysSince } == 0) {

                result.add(index, DayStatsItem("", 0, daysSince))
            } else {

                result.add(index, stats.single { it.day == daysSince }.mapToPresentation())
            }
        }

        result.sortBy(DayStatsItem::day)

        return result.map { statistic ->

            Entry(statistic.day.toFloat(), getHours(statistic.time).toFloat(), "")
        }
    }

    fun showRecordDialog() = showRecordDialog.call()
}
