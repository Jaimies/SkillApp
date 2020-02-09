package com.jdevs.timeo.model

import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.model.ViewType.STATISTIC

sealed class StatsItem : ViewItem {

    abstract val time: Long
    override val viewType = STATISTIC
}

object StatsTypes {

    const val DAY = 0
    const val WEEK = 1
    const val MONTH = 2
}

data class DayStatsItem(override val id: String, override val time: Long, val day: Long) :
    StatsItem()

data class WeekStatsItem(override val id: String, override val time: Long, val week: Int) :
    StatsItem()

data class MonthStatsItem(override val id: String, override val time: Long, val month: Int) :
    StatsItem()

fun DayStats.mapToPresentation() = DayStatsItem(id, time, day)
fun WeekStats.mapToPresentation() = WeekStatsItem(id, time, week)
fun MonthStats.mapToPresentation() = MonthStatsItem(id, time, month)
