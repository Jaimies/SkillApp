package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.domain.model.ActivityStatistic
import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {

    fun getDayStats(activityId: Id) =
        statsRepository.dayStats.getStatsByActivityId(activityId)

    fun getWeekStats(activityId: Id) =
        statsRepository.weekStats.getStatsByActivityId(activityId)

    fun getMonthStats(activityId: Id) =
        statsRepository.monthStats.getStatsByActivityId(activityId)

    private fun Flow<List<Statistic>>.getStatsByActivityId(activityId: Id): Flow<List<ActivityStatistic>> {
        return mapList { statistic ->
            statistic.toActivityStatistic(activityId)
        }
    }

    private fun Statistic.toActivityStatistic(activityId: Id): ActivityStatistic {
        return ActivityStatistic(date, time.getActivityTime(activityId))
    }
}
