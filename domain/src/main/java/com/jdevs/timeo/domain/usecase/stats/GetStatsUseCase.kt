package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {

    val dayStats get() = statsRepository.dayStats
    val weekStats get() = statsRepository.weekStats
    val monthStats get() = statsRepository.monthStats
    val dayStatsRemote get() = statsRepository.dayStatsRemote
    val weekStatsRemote get() = statsRepository.weekStatsRemote
    val monthStatsRemote get() = statsRepository.monthStatsRemote
}
