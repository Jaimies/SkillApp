package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.Entry
import com.maxpoliakov.skillapp.data.stats.DAY_STATS_ENTRIES
import com.maxpoliakov.skillapp.data.stats.STATS_ENTRIES
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.daysSinceEpoch
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import kotlin.math.ceil

private const val HOURS_DIVIDER = 120

data class StatsEntries(val entries: List<Entry>) {
    fun getMaximumValue(): Float {
        val maxValue = getHighestEntryValue()
        if (maxValue <= HOURS_DIVIDER) return maxValue.ceilTo(60)
        return maxValue.ceilTo(HOURS_DIVIDER)
    }

    private fun Float.ceilTo(to: Int) = ceil(this / to) * to
    private fun getHighestEntryValue() = entries.maxBy(Entry::getY)!!.y
}

fun List<Statistic>.withMissingStats(count: Int = DAY_STATS_ENTRIES): List<Statistic> {
    return List(count) { index ->
        val neededDate = getCurrentDate().minusDays(index.toLong())
        val item = find { chartItem -> chartItem.date == neededDate }

        if (item != null && item.time > Duration.ZERO) item
        else Statistic(neededDate, Duration.ZERO)
    }.sortedBy { it.date }
}

fun List<Statistic>.toStatsEntries(): StatsEntries? {
    if (!this.hasPositiveValues())
        return null

    return createEntries(this)
}

private fun createEntries(statsList: List<Statistic>): StatsEntries {
    return StatsEntries(statsList.getEntries())
}

private fun List<Statistic>.hasPositiveValues(): Boolean {
    return this.any { it.time > Duration.ZERO }
}

private fun List<Statistic>.getEntries(): List<Entry> {
    return takeLast(STATS_ENTRIES).toEntries()
}

private fun List<Statistic>.toEntries(): List<Entry> {
    return map { statistic ->
        Entry(statistic.date.daysSinceEpoch.toFloat(), statistic.time.toMinutes().toFloat())
    }
}
