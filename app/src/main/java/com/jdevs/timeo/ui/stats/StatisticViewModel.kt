package com.jdevs.timeo.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Stats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.util.time.getFriendlyDate
import com.jdevs.timeo.util.time.getFriendlyMonth
import com.jdevs.timeo.util.time.getFriendlyWeek
import com.jdevs.timeo.util.time.getHours

class StatisticViewModel : ViewModel() {

    val day get() = _day as LiveData<String>
    val time get() = _time as LiveData<String>

    private val _day = MutableLiveData("")
    private val _time = MutableLiveData("")

    fun setStats(stats: Stats) {

        _time.value = getHours(stats.time) + "h"

        _day.value = when (stats) {

            is DayStats -> getFriendlyDate(stats.day)
            is WeekStats -> getFriendlyWeek(stats.week)
            is MonthStats -> getFriendlyMonth(stats.month)
        }
    }
}
