package com.maxpoliakov.skillapp.shared.chart

import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval.Daily
import com.maxpoliakov.skillapp.domain.model.StatisticInterval.Yearly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDate

class StatsEntriesTest : StringSpec({
    "withMissingStats()" {
        val stats = listOf(
            Statistic(LocalDate.ofYearDay(2014, 1), Duration.ofHours(2).toMillis()),
            Statistic(LocalDate.ofYearDay(2020, 1), Duration.ofMinutes(32).toMillis())
        )

        stats.withMissingStats(Yearly, LocalDate.ofYearDay(2012, 1)..LocalDate.ofYearDay(2021, 1)) shouldBe listOf(
            Statistic(LocalDate.ofYearDay(2012, 1), 0),
            Statistic(LocalDate.ofYearDay(2013, 1), 0),
            Statistic(LocalDate.ofYearDay(2014, 1), Duration.ofHours(2).toMillis()),
            Statistic(LocalDate.ofYearDay(2015, 1), 0),
            Statistic(LocalDate.ofYearDay(2016, 1), 0),
            Statistic(LocalDate.ofYearDay(2017, 1), 0),
            Statistic(LocalDate.ofYearDay(2018, 1), 0),
            Statistic(LocalDate.ofYearDay(2019, 1), 0),
            Statistic(LocalDate.ofYearDay(2020, 1), Duration.ofMinutes(32).toMillis()),
            Statistic(LocalDate.ofYearDay(2021, 1), 0),
        )
    }

    "toStatsEntries() returns null for empty list" {
        val stats = listOf<Statistic>()
        stats.toEntries(Daily) shouldBe null
    }

    "toStatsEntries() returns null for list with no positive time" {
        val stats = listOf(Statistic(LocalDate.ofEpochDay(0), 0))
        stats.toEntries(Daily) shouldBe null
    }

    fun List<Entry>.toPairs() = map { it.x to it.y }

    "toStatsEntries() maps the original list to Entry" {
        val stats = listOf(
            Statistic(LocalDate.ofEpochDay(1), Duration.ofMinutes(20).toMillis()),
            Statistic(LocalDate.ofEpochDay(2), Duration.ofHours(2).toMillis())
        )

        val statsEntries = stats.toEntries(Daily)!!

        statsEntries.toPairs() shouldBe listOf(
            1f to Duration.ofMinutes(20).toMillis().toFloat(),
            2f to Duration.ofHours(2).toMillis().toFloat(),
        )
    }
})
