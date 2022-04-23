package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import java.time.LocalDate
import javax.inject.Inject

class GetTotalTimeAtDayUseCase @Inject constructor(
    private val statsRepository: StatsRepository
) {
    suspend fun run(date: LocalDate) = statsRepository.getCountAtDate(date)
}
