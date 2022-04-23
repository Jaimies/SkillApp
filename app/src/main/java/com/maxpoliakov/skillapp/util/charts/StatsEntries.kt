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

        if (item != null && item.count > 0) item
        else Statistic(neededDate, 0)
    }.sortedBy { it.date }
}

fun List<Statistic>.toEntries(
    unit: ChronoUnit = ChronoUnit.DAYS,
    convertDateToNumber: (date: LocalDate) -> Long = { date -> unit.between(EPOCH, date) }
): List<BarEntry>? {
    if (!this.hasPositiveValues())
        return null

    return this.convertToEntries(convertDateToNumber)
}

private fun List<Statistic>.hasPositiveValues(): Boolean {
    return this.any { it.count > 0 }
}

private fun List<Statistic>.convertToEntries(
    convertDateToNumber: (date: LocalDate) -> Long,
): List<BarEntry> {
    return map { statistic ->
        BarEntry(convertDateToNumber(statistic.date).toFloat(), (statistic.count / 1000).toFloat())
    }
}
