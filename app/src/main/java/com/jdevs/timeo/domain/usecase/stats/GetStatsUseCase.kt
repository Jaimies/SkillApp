package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.data.stats.StatsRepository
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val statsRepository: StatsRepository
) {

    val dayStats get() = statsRepository.dayStats
    val weekStats get() = statsRepository.weekStats
    val monthStats get() = statsRepository.monthStats

    fun resetDayStats() = statsRepository.resetDayStatsMonitor()
    fun resetWeekStats() = statsRepository.resetWeekStatsMonitor()
    fun resetMonthStats() = statsRepository.resetMonthStatsMonitor()
}
