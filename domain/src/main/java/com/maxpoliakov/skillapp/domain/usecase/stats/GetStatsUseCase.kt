package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import com.maxpoliakov.skillapp.shared.util.monthsSinceEpoch
import com.maxpoliakov.skillapp.shared.util.sumByLong
import com.maxpoliakov.skillapp.shared.util.weeksSinceEpoch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(private val statsRepository: StatsRepository) {
    fun getTimeToday(skillId: Id) = statsRepository.getCountAtDate(skillId, LocalDate.now())

    fun getTimeThisWeek(skillId: Id): Flow<Long> {
        val firstDayOfWeek = getCurrentDate().atStartOfWeek()
        val daysCount = firstDayOfWeek.until(getCurrentDate(), ChronoUnit.DAYS) + 1
        val dailyTimes = List(daysCount.toInt()) { index ->
            statsRepository.getCountAtDate(skillId, firstDayOfWeek.plusDays(index.toLong()))
        }

        return combine(dailyTimes) { times -> times.sum() }
    }

    fun getGroupTimeToday(groupId: Id) = statsRepository.getGroupCountAtDate(groupId, LocalDate.now())

    fun getGroupTimeThisWeek(skillId: Id): Flow<Long> {
        val firstDayOfWeek = getCurrentDate().atStartOfWeek()
        val daysCount = firstDayOfWeek.until(getCurrentDate(), ChronoUnit.DAYS) + 1
        val dailyTimes = List(daysCount.toInt()) { index ->
            statsRepository.getGroupCountAtDate(skillId, firstDayOfWeek.plusDays(index.toLong()))
        }

        return combine(dailyTimes) { times -> times.sum() }
    }

    fun getDailyStats(skillId: Id) = statsRepository.getStats(skillId, LocalDate.now().minusDays(56))

    fun getDailyStats(
        skillIds: List<Int>,
        startDate: LocalDate = LocalDate.now().minusDays(56)
    ): Flow<List<Statistic>> {
        return combine(skillIds.map { statsRepository.getStats(it, startDate) }) { statsLists ->
            groupByDate(statsLists)
        }
    }

    fun getWeeklyStats(skillIds: List<Int>): Flow<List<Statistic>> {
        return combine(skillIds.map { id -> getWeeklyStats(id) }) { statsLists ->
            groupByDate(statsLists)
        }
    }

    fun getMonthlyStats(skillIds: List<Int>): Flow<List<Statistic>> {
        return combine(skillIds.map { id -> getMonthlyStats(id) }) { statsLists ->
            groupByDate(statsLists)
        }
    }

    fun getWeeklyStats(skillId: Int): Flow<List<Statistic>> {
        val dailyStats = statsRepository.getStats(skillId, LocalDate.now().minusWeeks(7))

        return dailyStats.map { stats ->
            stats
                .groupBy { it.date.weeksSinceEpoch }
                .map { entry ->
                    Statistic(entry.value[0].date.atStartOfWeek(), entry.value.sumByLong(Statistic::count))
                }
        }
    }

    fun getMonthlyStats(skillId: Int): Flow<List<Statistic>> {
        val dailyStats = statsRepository.getStats(skillId, LocalDate.now().minusMonths(7))

        return dailyStats.map { stats ->
            stats
                .groupBy { it.date.monthsSinceEpoch }
                .map { entry ->
                    Statistic(entry.value[0].date.withDayOfMonth(1), entry.value.sumByLong(Statistic::count))
                }
        }
    }

    private fun groupByDate(statsLists: Array<List<Statistic>>) = statsLists
        .flatMap { it }
        .groupBy { it.date }
        .map { entry ->
            Statistic(entry.value[0].date, entry.value.sumByLong(Statistic::count))
        }
}
