package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.test.clockOfEpochDay
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Duration
import java.time.LocalDate

class StatsEntriesTest : StringSpec({
    beforeSpec {
        setClock(clockOfEpochDay(7))
    }

    "withMissingStats()" {
        val stats = listOf(
            Statistic(LocalDate.ofEpochDay(5), Duration.ofHours(2).toMillis()),
            Statistic(LocalDate.ofEpochDay(2), Duration.ofMinutes(32).toMillis())
        )

        stats.withMissingStats(count = 6) shouldBe listOf(
            Statistic(LocalDate.ofEpochDay(2), Duration.ofMinutes(32).toMillis()),
            Statistic(LocalDate.ofEpochDay(3), 0),
            Statistic(LocalDate.ofEpochDay(4), 0),
            Statistic(LocalDate.ofEpochDay(5), Duration.ofHours(2).toMillis()),
            Statistic(LocalDate.ofEpochDay(6), 0),
            Statistic(LocalDate.ofEpochDay(7), 0)
        )
    }

    "toStatsEntries() returns null for empty list" {
        val stats = listOf<Statistic>()
        stats.toEntries() shouldBe null
    }

    "toStatsEntries() returns null for list with no positive time" {
        val stats = listOf(Statistic(LocalDate.ofEpochDay(0), 0))
        stats.toEntries() shouldBe null
    }

    fun List<Entry>.toPairs() = map { it.x to it.y }

    "toStatsEntries() maps the original list to Entry" {
        val stats = listOf(
            Statistic(LocalDate.ofEpochDay(1), Duration.ofMinutes(20).toMillis()),
            Statistic(LocalDate.ofEpochDay(2), Duration.ofHours(2).toMillis())
        )

        val statsEntries = stats.toEntries()!!
        statsEntries.toPairs() shouldBe listOf(
            1f to Duration.ofMinutes(20).toMillis().toFloat(),
            2f to Duration.ofHours(2).toMillis().toFloat(),
        )
    }

    afterSpec {
        setClock(Clock.systemDefaultZone())
    }
})
