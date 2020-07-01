package com.jdevs.timeo.domain.usecase.stats

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.ActivityStatistic
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.mapList
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {

    fun getDayStats(activityId: String) = statsRepository.dayStats.getStatistics(activityId)
    fun getWeekStats(activityId: String) = statsRepository.weekStats.getStatistics(activityId)
    fun getMonthStats(activityId: String) = statsRepository.monthStats.getStatistics(activityId)

    private fun LiveData<List<Statistic>>.getStatistics(activityId: String) = mapList { stat ->
        ActivityStatistic(
            if (activityId == "") stat.time else stat.activityTimes[activityId] ?: 0,
            stat.day
        )
    }
}
