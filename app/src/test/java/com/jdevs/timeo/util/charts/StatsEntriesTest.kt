package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.jdevs.timeo.clockOfEpochDay
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.shared.util.setClock
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
            Statistic(LocalDate.ofEpochDay(5), Duration.ofHours(2)),
            Statistic(LocalDate.ofEpochDay(2), Duration.ofMinutes(32))
        )

        stats.withMissingStats(6) shouldBe listOf(
            Statistic(LocalDate.ofEpochDay(2), Duration.ofMinutes(32)),
            Statistic(LocalDate.ofEpochDay(3), Duration.ZERO),
            Statistic(LocalDate.ofEpochDay(4), Duration.ZERO),
            Statistic(LocalDate.ofEpochDay(5), Duration.ofHours(2)),
            Statistic(LocalDate.ofEpochDay(6), Duration.ZERO),
            Statistic(LocalDate.ofEpochDay(7), Duration.ZERO)
        )
    }

    "toStatsEntries() returns null for empty list" {
        val stats = listOf<Statistic>()
        stats.toStatsEntries() shouldBe null
    }

    "toStatsEntries() returns null for list with no positive time" {
        val stats = listOf(Statistic(LocalDate.ofEpochDay(0), Duration.ZERO))
        stats.toStatsEntries() shouldBe null
    }

    fun List<Entry>.toPairs() = map { it.x to it.y }

    "toStatsEntries() splits the list in two parts and increases x of the first one by 7" {
        val stats = listOf(
            Statistic(LocalDate.ofEpochDay(-1), Duration.ofHours(2)),
            Statistic(LocalDate.ofEpochDay(1), Duration.ofMinutes(20))
        ).withMissingStats()

        val statsEntries = stats.toStatsEntries()!!
        statsEntries.entries.toPairs() shouldBe listOf(
            1f to 20f,
            2f to 0f,
            3f to 0f,
            4f to 0f,
            5f to 0f,
            6f to 0f,
            7f to 0f
        )

        statsEntries.previousEntries!!.toPairs() shouldBe listOf(
            1f to 0f,
            2f to 0f,
            3f to 0f,
            4f to 0f,
            5f to 0f,
            6f to 120f,
            7f to 0f
        )
    }

    afterSpec {
        setClock(Clock.systemDefaultZone())
    }
})
