package com.jdevs.timeo.domain.model

/** Key for id, value for time */
typealias ActivityTimes = Map<String, Int>

data class DayStats(val time: Int, val day: Int, val activityTimes: ActivityTimes)
data class WeekStats(val time: Int, val week: Int, val activityTimes: ActivityTimes)
data class MonthStats(val time: Int, val month: Int, val activityTimes: ActivityTimes)

data class ActivityDayStats(val time: Int, val day: Int)
data class ActivityWeekStats(val time: Int, val week: Int)
data class ActivityMonthStats(val time: Int, val month: Int)
