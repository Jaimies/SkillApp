package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {
    fun run(skillId: Id) = statsRepository.getStats(skillId)

    fun run(skillIds: List<Int>): Flow<List<Statistic>> {
        return combine(skillIds.map(this::run)) { statsLists ->
            statsLists
                .flatMap { it }
                .groupBy { it.date }
                .map { entry ->
                    Statistic(entry.value[0].date, entry.value.sumByDuration(Statistic::time))
                }
        }
    }
}
