package com.jdevs.timeo.domain.usecase.stats

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {
    fun run(activityId: Id) = statsRepository.getStats(activityId)
}
