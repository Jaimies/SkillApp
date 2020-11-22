package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.data.stats.STATS_ENTRIES
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChartState(val entries: StatsEntries, val formatter: ValueFormatter) {
    companion object {
        operator fun invoke(
            entries: StatsEntries?, formatter: ValueFormatter
        ) = entries?.let { ChartState(entries, formatter) }
    }
}

fun List<DayStatistic>.toEntries(
    timeUnit: ChronoUnit, entriesCount: Int = STATS_ENTRIES
): StatsEntries? {

    if (!this.hasPositiveValues())
        return null

    val statsList = createStatsList(entriesCount, timeUnit)
    return createEntries(statsList)
}

private fun createEntries(statsList: List<DayStatistic>): StatsEntries {
    val entries = statsList.getEntries()
    val previousEntries = statsList.getPrevEntries()

    return StatsEntries(entries, previousEntries)
}

private fun List<DayStatistic>.hasPositiveValues() =
    this.any { it.time > Duration.ZERO }

private fun List<DayStatistic>.createStatsList(
    entriesCount: Int, timeUnit: ChronoUnit
): List<DayStatistic> {

    return List(entriesCount) { index ->
        val neededDate = getCurrentDate().minus(index.toLong(), timeUnit)
        val item = find { chartItem -> chartItem.date == neededDate }

        if (item != null && item.time > Duration.ZERO) item
        else DayStatistic(neededDate, Duration.ZERO)
    }.sortedBy { it.date }
}

private fun List<DayStatistic>.getPrevEntries(): List<Entry>? {
    if (this.size > STATS_ENTRIES)
        return createPrevEntries()

    return null
}

private fun List<DayStatistic>.createPrevEntries(): List<Entry> {
    return take(STATS_ENTRIES).map(::mapPrevStat)
}

private fun mapPrevStat(item: DayStatistic) =
    Entry(item.date.daysSinceEpoch.toFloat() + STATS_ENTRIES, item.time.toMinutes().toFloat())

private fun List<DayStatistic>.getEntries(): List<Entry> {
    return takeLast(STATS_ENTRIES).map { item ->
        Entry(item.date.daysSinceEpoch.toFloat(), item.time.toMinutes().toFloat())
    }
}
