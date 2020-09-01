package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.domain.model.ActivityStatistic
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {

    fun getDayStats(activityId: Int) =
        statsRepository.dayStats.getStatsByActivityId(activityId)

    fun getWeekStats(activityId: Int) =
        statsRepository.weekStats.getStatsByActivityId(activityId)

    fun getMonthStats(activityId: Int) =
        statsRepository.monthStats.getStatsByActivityId(activityId)

    private fun Flow<List<Statistic>>.getStatsByActivityId(activityId: Int) =
        mapList { statistic -> statistic.toActivityStat(activityId) }

    private fun Statistic.toActivityStat(activityId: Int): ActivityStatistic {
        return ActivityStatistic(getActivityTime(activityId), day)
    }

    private fun Statistic.getActivityTime(activityId: Int): Int {
        if (activityId != -1) return activityTimes[activityId] ?: 0
        return time
    }
}
