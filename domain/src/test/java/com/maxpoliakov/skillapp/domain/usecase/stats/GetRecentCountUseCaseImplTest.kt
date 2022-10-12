package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.domain.usecase.stub.StubStatsRepository
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.await
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class GetRecentCountUseCaseImplTest : StringSpec({
    "getTimeToday()" {
        val statsRepository = StubStatsRepository()
        val useCase = GetRecentCountUseCaseImpl(statsRepository)

        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Daily).await() shouldBe 2L
    }

    "getTimeThisWeek()" {
        val statsRepository = StubStatsRepository()
        val useCase = GetRecentCountUseCaseImpl(statsRepository)

        Locale.setDefault(Locale.UK)

        setClock(Clock.fixed(Instant.parse("2020-01-06T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 2L

        setClock(Clock.fixed(Instant.parse("2020-01-07T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 4L

        setClock(Clock.fixed(Instant.parse("2020-01-08T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 6L

        setClock(Clock.fixed(Instant.parse("2020-01-09T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 8L

        setClock(Clock.fixed(Instant.parse("2020-01-10T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 10L

        setClock(Clock.fixed(Instant.parse("2020-01-11T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 12L

        setClock(Clock.fixed(Instant.parse("2020-01-12T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 14L

        setClock(Clock.fixed(Instant.parse("2020-01-13T12:15:30.00Z"), ZoneId.systemDefault()))
        useCase.getCountSinceStartOfInterval(1, StatisticInterval.Weekly).await() shouldBe 2L
    }
})
