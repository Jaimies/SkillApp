package com.jdevs.timeo.domain.model

import com.jdevs.timeo.shared.util.sumByDuration
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate

class StatisticTime(private val timeMap: Map<Id, Duration>) {
    fun getActivityTime(activityId: Id): Duration {
        return timeMap[activityId] ?: Duration.ZERO
    }

    fun getTotalTime() = timeMap.sumByDuration()
}

data class Statistic(val date: LocalDate, val time: StatisticTime)
data class ActivityStatistic(val date: LocalDate, val time: Duration)
