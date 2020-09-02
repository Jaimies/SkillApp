package com.jdevs.timeo.domain.model

import com.jdevs.timeo.shared.util.sumBy

class StatisticTime(private val timeMap: Map<Int, Int>) {
    fun getActivityTime(activityId: Int) = timeMap[activityId] ?: 0
    fun getTotalTime() = timeMap.sumBy { entry -> entry.value }
}

data class Statistic(val day: Int, val time: StatisticTime)
data class ActivityStatistic(val day: Int, val time: Int)
