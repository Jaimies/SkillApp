package com.jdevs.timeo.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.util.getFriendlyDate
import com.jdevs.timeo.data.util.getFriendlyMonth
import com.jdevs.timeo.data.util.getFriendlyWeek
import com.jdevs.timeo.data.util.getHours
import com.jdevs.timeo.ui.model.DayStatsItem
import com.jdevs.timeo.ui.model.MonthStatsItem
import com.jdevs.timeo.ui.model.StatsItem
import com.jdevs.timeo.ui.model.WeekStatsItem

class StatisticViewModel : ViewModel() {

    val day get() = _day as LiveData<String>
    val time get() = _time as LiveData<String>

    private val _day = MutableLiveData("")
    private val _time = MutableLiveData("")

    fun setStats(stats: StatsItem) {

        _time.value = getHours(stats.time) + "h"

        _day.value = when (stats) {

            is DayStatsItem -> getFriendlyDate(stats.day)
            is WeekStatsItem -> getFriendlyWeek(stats.week)
            is MonthStatsItem -> getFriendlyMonth(stats.month)
        }
    }
}
