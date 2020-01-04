package com.jdevs.timeo.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.MonthStats
import com.jdevs.timeo.data.Stats
import com.jdevs.timeo.data.WeekStats
import com.jdevs.timeo.util.time.toFriendlyDate
import com.jdevs.timeo.util.time.toFriendlyMonth
import com.jdevs.timeo.util.time.toFriendlyWeek
import com.jdevs.timeo.util.time.toHours

class StatisticViewModel : ViewModel() {

    val day get() = _day as LiveData<String>
    val time get() = _time as LiveData<String>

    private val _day = MutableLiveData("")
    private val _time = MutableLiveData("")

    fun setStats(stats: Stats) {

        stats.setupTime()
        _time.value = stats.time.toHours() + "h"

        _day.value = when (stats) {

            is DayStats -> stats.day.toFriendlyDate()
            is WeekStats -> stats.week.toFriendlyWeek()
            is MonthStats -> stats.month.toFriendlyMonth()
        }
    }
}