package com.jdevs.timeo.domain.model

import com.jdevs.timeo.ui.common.adapter.ViewItem
import com.jdevs.timeo.util.ViewTypes.STATISTIC

sealed class Stats : ViewItem {

    abstract val time: Long
    override val viewType = STATISTIC
}

data class DayStats(
    override val documentId: String = "",
    override val time: Long,
    val day: Long
) : Stats() {

    override val id
        get() = day.toInt()
}

data class WeekStats(
    override val documentId: String = "",
    override val time: Long,
    val week: Int
) : Stats() {

    override val id
        get() = week
}

data class MonthStats(
    override val documentId: String = "",
    override val time: Long,
    val month: Int
) : Stats() {

    override val id
        get() = month
}
