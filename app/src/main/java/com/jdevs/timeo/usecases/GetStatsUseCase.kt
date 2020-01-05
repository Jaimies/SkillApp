package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.source.StatsRepository
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
