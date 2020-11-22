package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {
    fun getDayStats(activityId: Id) = statsRepository.getDayStats(activityId)
    fun getWeekStats(activityId: Id) = statsRepository.getWeekStats(activityId)
    fun getMonthStats(activityId: Id) = statsRepository.getMonthStats(activityId)
}
