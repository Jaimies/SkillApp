package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.await
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class StubStatsRepository(
    private val stats: Map<Int, List<Statistic>>,
) : StatsRepository {

    override fun getStats(skillId: Id, startDate: LocalDate): Flow<List<Statistic>> {
        return flowOf(stats[skillId]!!)
    }

    override fun getTimeToday(skillId: Id) = flowOf(Duration.ofHours(2))
    override fun getTimeAtDate(skillId: Id, date: LocalDate) = flowOf(Duration.ofHours(2))
    override fun getGroupTimeAtDate(groupId: Id, date: LocalDate) = flowOf(Duration.ofHours(2))

    override suspend fun addRecord(record: Record) {}
    override suspend fun getTimeAtDate(date: LocalDate): Duration = Duration.ofHours(2)
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

    "getTimeThisWeek()" {
        val statsRepository = StubStatsRepository(mapOf())
        val useCase = GetStatsUseCase(statsRepository)

        Locale.setDefault(Locale.UK)

        setClock(Clock.fixed(Instant.parse("2020-01-06T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(2)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(2)

        setClock(Clock.fixed(Instant.parse("2020-01-07T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(4)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(4)

        setClock(Clock.fixed(Instant.parse("2020-01-08T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(6)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(6)

        setClock(Clock.fixed(Instant.parse("2020-01-09T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(8)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(8)

        setClock(Clock.fixed(Instant.parse("2020-01-10T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(10)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(10)

        setClock(Clock.fixed(Instant.parse("2020-01-11T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(12)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(12)

        setClock(Clock.fixed(Instant.parse("2020-01-12T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(14)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(14)

        setClock(Clock.fixed(Instant.parse("2020-01-13T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getTimeThisWeek(1).await() shouldBe Duration.ofHours(2)
        useCase.getGroupTimeThisWeek(1).await() shouldBe Duration.ofHours(2)
    }
})
