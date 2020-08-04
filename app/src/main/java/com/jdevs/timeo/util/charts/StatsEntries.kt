package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
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