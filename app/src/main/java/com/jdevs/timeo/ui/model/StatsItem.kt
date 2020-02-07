package com.jdevs.timeo.ui.model

import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.ui.model.ViewType.STATISTIC

sealed class StatsItem : ViewItem {

    abstract val time: Long
    override val viewType = STATISTIC
}

object StatsTypes {

    const val DAY = 0
    const val WEEK = 1
    const val MONTH = 2
}

data class DayStatsItem(
    override val documentId: String = "",
    override val time: Long,
    val day: Long
) : StatsItem() {

    override val id
        get() = day.toInt()
}

data class WeekStatsItem(
    override val documentId: String = "",
    override val time: Long,
    val week: Int
) : StatsItem() {

    override val id
        get() = week
}

data class MonthStatsItem(
    override val documentId: String = "",
    override val time: Long,
    val month: Int
) : StatsItem() {

    override val id
        get() = month
}

fun DayStats.mapToPresentation() = DayStatsItem(documentId, time, day)
fun WeekStats.mapToPresentation() = WeekStatsItem(documentId, time, week)
fun MonthStats.mapToPresentation() = MonthStatsItem(documentId, time, month)
