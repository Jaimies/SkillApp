package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.jdevs.timeo.data.stats.DAY_STATS_ENTRIES
import com.jdevs.timeo.data.stats.STATS_ENTRIES
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.getCurrentDate
import java.time.Duration
import kotlin.math.ceil
import kotlin.math.max

const val HOURS_BREAKPOINT = 160f
private const val HOURS_DIVIDER = 120

data class StatsEntries(
    val entries: List<Entry>,
    val previousEntries: List<Entry>?
) {
    fun getMaximumValue(): Float {
        val maxValue = getHighestEntryValue()

        if (maxValue <= HOURS_BREAKPOINT) return ceil(maxValue)
        return maxValue.ceilTo(HOURS_DIVIDER)
    }

    private fun Float.ceilTo(to: Int) = ceil(this / to) * to

    private fun getHighestEntryValue(): Float {
        return max(
            entries.getHighestEntryValue(),
            previousEntries?.getHighestEntryValue() ?: 0f
        )
    }
}

private fun List<Entry>.getHighestEntryValue(): Float {
    return this.maxBy(Entry::getY)!!.y
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
    val entries = statsList.getEntries()
    val previousEntries = statsList.getPrevEntries()

    return StatsEntries(entries, previousEntries)
}

private fun List<Statistic>.hasPositiveValues(): Boolean {
    return this.any { it.time > Duration.ZERO }
}

private fun List<Statistic>.getPrevEntries(): List<Entry>? {
    return take(STATS_ENTRIES).toEntries().map {
        Entry(it.x + STATS_ENTRIES, it.y)
    }
}

private fun List<Statistic>.getEntries(): List<Entry> {
    return takeLast(STATS_ENTRIES).toEntries()
}

private fun List<Statistic>.toEntries(): List<Entry> {
    return map { statistic ->
        Entry(statistic.date.daysSinceEpoch.toFloat(), statistic.time.toMinutes().toFloat())
    }
}
