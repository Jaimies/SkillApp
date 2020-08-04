package com.jdevs.timeo.domain.usecase.stats

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.ActivityStatistic
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.mapList
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {

    fun getDayStats(activityId: String) =
        statsRepository.dayStats.getStatsByActivityId(activityId)

    fun getWeekStats(activityId: String) =
        statsRepository.weekStats.getStatsByActivityId(activityId)

    fun getMonthStats(activityId: String) =
        statsRepository.monthStats.getStatsByActivityId(activityId)

    private fun LiveData<List<Statistic>>.getStatsByActivityId(activityId: String) =
        mapList { statistic -> statistic.toActivityStat(activityId) }

    private fun Statistic.toActivityStat(activityId: String): ActivityStatistic {
        return ActivityStatistic(getActivityTime(activityId), day)
    }

    private fun Statistic.getActivityTime(activityId: String): Int {
        if (activityId != "") return activityTimes[activityId] ?: 0
        return time
    }
}
