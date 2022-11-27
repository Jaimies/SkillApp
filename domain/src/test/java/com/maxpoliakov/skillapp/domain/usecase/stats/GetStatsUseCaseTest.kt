package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.usecase.stub.StubSkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import com.maxpoliakov.skillapp.test.await
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class GetStatsUseCaseTest : StringSpec({
    "gets stats for multiple skills" {
        val referenceDate = LocalDate.now().atStartOfWeek()

        val skillStats = listOf(
            Statistic(referenceDate.plusDays(1), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.plusDays(3), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.plusDays(4), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.minusDays(4), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.minusDays(5), Duration.ofHours(2).toMillis()),
            Statistic(referenceDate.minusDays(15), Duration.ofHours(2).toMillis()),
        )
        val statsRepository = StubSkillStatsRepository(
            mapOf(
                1 to skillStats,
                2 to skillStats,
            )
        )

        val useCase = GetStatsUseCaseImpl(statsRepository)

        useCase.getStats(listOf(1, 2), LocalDate.now()..LocalDate.now(),StatisticInterval.Weekly).await() shouldBe listOf(
            Statistic(referenceDate, Duration.ofHours(12).toMillis()),
            Statistic(referenceDate.minusWeeks(1), Duration.ofHours(8).toMillis()),
            Statistic(referenceDate.minusWeeks(3), Duration.ofHours(4).toMillis()),
        )
    }
})
