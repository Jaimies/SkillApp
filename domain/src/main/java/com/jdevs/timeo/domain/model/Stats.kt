package com.jdevs.timeo.domain.model

data class DayStats(val id: String = "", val time: Long, val day: Long)
data class WeekStats(val id: String = "", val time: Long, val week: Int)
data class MonthStats(val id: String = "", val time: Long, val month: Int)
