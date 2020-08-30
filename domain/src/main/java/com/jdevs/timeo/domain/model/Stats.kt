package com.jdevs.timeo.domain.model

/** Key for id, value for time */
typealias ActivityTimes = Map<Int, Int>

data class Statistic(val time: Int, val day: Int, val activityTimes: ActivityTimes)
data class ActivityStatistic(val time: Int, val day: Int)
