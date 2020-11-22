package com.jdevs.timeo.util.charts

import com.jdevs.timeo.data.stats.DAY_STATS_ENTRIES
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit.DAYS

class ChartStateTest : StringSpec({
    "returns null for empty list" {
        val stats = listOf<DayStatistic>()
        stats.toEntries(DAYS) shouldBe null
    }

    "returns null for list with no positive time" {
        val stats = listOf(DayStatistic(LocalDate.now(), Duration.ZERO))
        stats.toEntries(DAYS) shouldBe null
    }

    "returns the list with added missing items" {
        setClock(Clock.fixed(Instant.parse("1970-01-07T00:00:00Z"), ZoneId.systemDefault()))

        val stats = listOf(
            DayStatistic(LocalDate.parse("1970-01-05"), Duration.ofHours(2)),
            DayStatistic(LocalDate.parse("1969-12-30"), Duration.ofMinutes(32))
        )

        val statsEntries = stats.toEntries(DAYS, DAY_STATS_ENTRIES)!!
        statsEntries.entries.map { it.x to it.y } shouldBe listOf(
            0f to 0f,
            1f to 0f,
            2f to 0f,
            3f to 0f,
            4f to 120f,
            5f to 0f,
            6f to 0f
        )

        statsEntries.previousEntries!!.map { it.x to it.y } shouldBe listOf(
            0f to 0f,
            1f to 0f,
            2f to 0f,
            3f to 0f,
            4f to 0f,
            5f to 32f,
            6f to 0f
        )

        setClock(Clock.systemDefaultZone())
    }
})
