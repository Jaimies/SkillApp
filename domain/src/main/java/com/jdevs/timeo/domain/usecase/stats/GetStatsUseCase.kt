package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.domain.model.ActivityDayStats
import com.jdevs.timeo.domain.model.ActivityMonthStats
import com.jdevs.timeo.domain.model.ActivityWeekStats
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.mapList
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {

    fun getDayStats(activityId: String) = statsRepository.dayStats.mapList { stats ->
        ActivityDayStats(stats.activityTimes[activityId] ?: 0, stats.day)
    }

    fun getWeekStats(activityId: String) = statsRepository.weekStats.mapList { statistic ->
        ActivityWeekStats(statistic.activityTimes[activityId] ?: 0, statistic.week)
    }

    fun getMonthStats(activityId: String) = statsRepository.monthStats.mapList { statistic ->
        ActivityMonthStats(statistic.activityTimes[activityId] ?: 0, statistic.month)
    }
}
