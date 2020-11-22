package com.jdevs.timeo.util.charts

import com.jdevs.timeo.clockOfEpochDay
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class StatsEntriesTest : StringSpec({
    beforeSpec {
        setClock(clockOfEpochDay(7))
    }

    "withMissingStats()" {
        val stats = listOf(
            DayStatistic(LocalDate.ofEpochDay(5), Duration.ofHours(2)),
            DayStatistic(LocalDate.ofEpochDay(2), Duration.ofMinutes(32))
        )

        stats.withMissingStats(5, DAYS) shouldBe listOf(
            DayStatistic(LocalDate.ofEpochDay(2), Duration.ofMinutes(32)),
            DayStatistic(LocalDate.ofEpochDay(3), Duration.ZERO),
            DayStatistic(LocalDate.ofEpochDay(4), Duration.ZERO),
            DayStatistic(LocalDate.ofEpochDay(5), Duration.ofHours(2)),
            DayStatistic(LocalDate.ofEpochDay(6), Duration.ZERO)
        )
    }

    afterSpec {
        setClock(Clock.systemDefaultZone())
    }
})
