package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.BarEntry
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.LocalDate

fun List<Statistic>.withMissingStats(interval: StatisticInterval): List<Statistic> {
    return List(interval.numberOfValues) { index ->
        val neededDate = interval
            .atStartOfInterval(getCurrentDate())
            .minus(index.toLong(), interval.unit)

        val item = find { chartItem -> chartItem.date == neededDate }

        if (item != null && item.count > 0) item
        else Statistic(neededDate, 0)
    }.sortedBy { it.date }
}

fun List<Statistic>.toEntries(interval: StatisticInterval): List<BarEntry>? {
    if (this.hasNoPositiveValues()) return null
    return this.convertToEntries(interval::toNumber)
}

private fun List<Statistic>.hasNoPositiveValues(): Boolean {
    return this.none { it.count > 0 }
}

private fun List<Statistic>.convertToEntries(
    convertDateToNumber: (date: LocalDate) -> Long,
): List<BarEntry> {
    return map { statistic ->
        BarEntry(convertDateToNumber(statistic.date).toFloat(), statistic.count.toFloat())
    }
}
