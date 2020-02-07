package com.jdevs.timeo.domain.model

data class DayStats(
    val documentId: String = "",
    val time: Long,
    val day: Long
)

data class WeekStats(
    val documentId: String = "",
    val time: Long,
    val week: Int
)

data class MonthStats(
    val documentId: String = "",
    val time: Long,
    val month: Int
)
