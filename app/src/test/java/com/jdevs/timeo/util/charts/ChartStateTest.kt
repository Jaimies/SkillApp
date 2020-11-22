package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.clockOfEpochDay
import com.jdevs.timeo.data.stats.DAY_STATS_ENTRIES
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.shared.util.setClock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

class StubFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return ""
    }
}

class ChartStateTest : StringSpec({
    beforeSpec {
        setClock(clockOfEpochDay(7))
    }

    val formatter = StubFormatter()

    "toChartState() returns null for empty list" {
        val stats = listOf<DayStatistic>()
        stats.toChartState(formatter) shouldBe null
    }

    "toChartState() returns null for list with no positive time" {
        val stats = listOf(DayStatistic(LocalDate.ofEpochDay(0), Duration.ZERO))
        stats.toChartState(formatter) shouldBe null
    }

    "toChartState() splits the list in two parts and increases x of the first one by 7" {
        fun List<Entry>.toPairs() = map { it.x to it.y }

        val stats = listOf(
            DayStatistic(LocalDate.ofEpochDay(-1), Duration.ofHours(2)),
            DayStatistic(LocalDate.ofEpochDay(0), Duration.ofMinutes(20))
        ).withMissingStats(DAY_STATS_ENTRIES, DAYS)

        val statsEntries = stats.toChartState(formatter)!!.entries
        statsEntries.entries.toPairs() shouldBe listOf(
            0f to 20f,
            1f to 0f,
            2f to 0f,
            3f to 0f,
            4f to 0f,
            5f to 0f,
            6f to 0f
        )

        statsEntries.previousEntries!!.toPairs() shouldBe listOf(
            0f to 0f,
            1f to 0f,
            2f to 0f,
            3f to 0f,
            4f to 0f,
            5f to 0f,
            6f to 120f
        )
    }

    afterSpec {
        setClock(Clock.systemDefaultZone())
    }
})
