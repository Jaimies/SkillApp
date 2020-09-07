package com.jdevs.timeo.domain.model

import com.jdevs.timeo.shared.util.sumBy
import org.threeten.bp.LocalDate

class StatisticTime(private val timeMap: Map<Id, Int>) {
    fun getActivityTime(activityId: Id) = timeMap[activityId] ?: 0
    fun getTotalTime() = timeMap.sumBy { entry -> entry.value }
}

data class Statistic(val date: LocalDate, val time: StatisticTime)
data class ActivityStatistic(val date: LocalDate, val time: Int)
