package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.data.stats.STATS_ENTRIES
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.daysSinceEpoch
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration

fun List<Statistic>.withMissingStats(count: Int = STATS_ENTRIES): List<Statistic> {
    return List(count) { index ->
        val neededDate = getCurrentDate().minusDays(index.toLong())
        val item = find { chartItem -> chartItem.date == neededDate }

        if (item != null && item.time > Duration.ZERO) item
        else Statistic(neededDate, Duration.ZERO)
    }.sortedBy { it.date }
}

fun List<Statistic>.toEntries(): List<Entry>? {
    if (!this.hasPositiveValues())
        return null

    return this.convertToEntries()
}

private fun List<Statistic>.hasPositiveValues(): Boolean {
    return this.any { it.time > Duration.ZERO }
}

private fun List<Statistic>.convertToEntries(): List<Entry> {
    return map { statistic ->
        Entry(statistic.date.daysSinceEpoch.toFloat(), statistic.time.toMinutes().toFloat())
    }
}
