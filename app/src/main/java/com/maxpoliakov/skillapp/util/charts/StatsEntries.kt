package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun List<Statistic>.withMissingStats(unit: ChronoUnit = ChronoUnit.DAYS, startDate: LocalDate = getCurrentDate(), count: Int = 7): List<Statistic> {
    return List(count) { index ->
        val neededDate = startDate.minus(index.toLong(), unit)

        val item = find { chartItem -> chartItem.date == neededDate }

        if (item != null && item.time > Duration.ZERO) item
        else Statistic(neededDate, Duration.ZERO)
    }.sortedBy { it.date }
}

fun List<Statistic>.toEntries(unit: ChronoUnit = ChronoUnit.DAYS): List<BarEntry>? {
    if (!this.hasPositiveValues())
        return null

    return this.convertToEntries(unit)
}

private fun List<Statistic>.hasPositiveValues(): Boolean {
    return this.any { it.time > Duration.ZERO }
}

private fun List<Statistic>.convertToEntries(unit: ChronoUnit): List<BarEntry> {
    return map { statistic ->
        BarEntry(unit.between(EPOCH, statistic.date).toFloat(), statistic.time.seconds.toFloat())
    }
}
