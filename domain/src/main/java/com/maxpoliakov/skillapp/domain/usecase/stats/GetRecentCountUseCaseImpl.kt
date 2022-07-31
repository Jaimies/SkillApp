package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.temporal.ChronoUnit

open class GetRecentCountUseCaseImpl(
    private val statsRepository: StatsRepository
) : GetRecentCountUseCase {

    override fun getCountToday(id: Id): Flow<Long> {
        return statsRepository.getCountAtDate(id, LocalDate.now())
    }

    override fun getCountThisWeek(id: Id): Flow<Long> {
        val firstDayOfWeek = getCurrentDate().atStartOfWeek()
        val daysCount = firstDayOfWeek.until(getCurrentDate(), ChronoUnit.DAYS) + 1
        val dailyTimes = List(daysCount.toInt()) { index ->
            statsRepository.getCountAtDate(id, firstDayOfWeek.plusDays(index.toLong()))
        }

        return combine(dailyTimes) { times -> times.sum() }
    }
}
