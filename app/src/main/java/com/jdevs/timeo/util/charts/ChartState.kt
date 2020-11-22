package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.data.stats.STATS_ENTRIES
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.shared.util.daysSinceEpoch
import java.time.Duration

class ChartState(val entries: StatsEntries, val formatter: ValueFormatter)

fun List<DayStatistic>.toChartState(formatter: ValueFormatter): ChartState? {
    if (!this.hasPositiveValues())
        return null

    return ChartState(createEntries(this), formatter)
}

private fun createEntries(statsList: List<DayStatistic>): StatsEntries {
    val entries = statsList.getEntries()
    val previousEntries = statsList.getPrevEntries()

    return StatsEntries(entries, previousEntries)
}

private fun List<DayStatistic>.hasPositiveValues(): Boolean {
    return this.any { it.time > Duration.ZERO }
}

private fun List<DayStatistic>.getPrevEntries(): List<Entry>? {
    if (this.size > STATS_ENTRIES)
        return createPrevEntries()

    return null
}

private fun List<DayStatistic>.createPrevEntries(): List<Entry> {
    return take(STATS_ENTRIES).toEntries().map {
        Entry(it.x + STATS_ENTRIES, it.y)
    }
}

private fun List<DayStatistic>.getEntries(): List<Entry> {
    return takeLast(STATS_ENTRIES).toEntries()
}

private fun List<DayStatistic>.toEntries(): List<Entry> {
    return map { statistic ->
        Entry(statistic.date.daysSinceEpoch.toFloat(), statistic.time.toMinutes().toFloat())
    }
}
