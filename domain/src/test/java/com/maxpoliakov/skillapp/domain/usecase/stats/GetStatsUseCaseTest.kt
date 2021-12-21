package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.test.await
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Duration
import java.time.LocalDate

class StubStatsRepository(
    private val stats: Map<Int, List<Statistic>>,
) : StatsRepository {

    override fun getStats(skillId: Id, startDate: LocalDate): Flow<List<Statistic>> {
        return flowOf(stats[skillId]!!)
    }

    override suspend fun addRecord(record: Record) {}
    override suspend fun getTimeAtDate(date: LocalDate): Duration = Duration.ZERO
}

class GetStatsUseCaseTest : StringSpec({
    "gets stats for multiple skills" {
        val referenceDate = LocalDate.now().atStartOfWeek()

        val skillStats = listOf(
            Statistic(referenceDate.plusDays(1), Duration.ofHours(2)),
            Statistic(referenceDate.plusDays(3), Duration.ofHours(2)),
            Statistic(referenceDate.plusDays(4), Duration.ofHours(2)),
            Statistic(referenceDate.minusDays(4), Duration.ofHours(2)),
            Statistic(referenceDate.minusDays(5), Duration.ofHours(2)),
            Statistic(referenceDate.minusDays(15), Duration.ofHours(2)),
        )
        val statsRepository = StubStatsRepository(
            mapOf(
                1 to skillStats,
                2 to skillStats,
            )
        )

        val useCase = GetStatsUseCase(statsRepository)

        useCase.getWeeklyStats(listOf(1, 2)).await() shouldBe listOf(
            Statistic(referenceDate, Duration.ofHours(12)),
            Statistic(referenceDate.minusWeeks(1), Duration.ofHours(8)),
            Statistic(referenceDate.minusWeeks(3), Duration.ofHours(4)),
        )
    }
})
