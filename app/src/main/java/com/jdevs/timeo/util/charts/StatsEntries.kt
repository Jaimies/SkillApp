package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.shared.util.getCurrentDate
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.math.ceil
import kotlin.math.max

const val HOURS_BREAKPOINT = 4
private const val HOURS_DIVIDER = 3

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

fun List<DayStatistic>.withMissingStats(
    entriesCount: Int, timeUnit: ChronoUnit
): List<DayStatistic> {

    return List(entriesCount) { index ->
        val neededDate = getCurrentDate().minus(index.toLong(), timeUnit)
        val item = find { chartItem -> chartItem.date == neededDate }

        if (item != null && item.time > Duration.ZERO) item
        else DayStatistic(neededDate, Duration.ZERO)
    }.sortedBy { it.date }
}
