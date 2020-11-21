package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.data.stats.STATS_ENTRIES
import com.jdevs.timeo.domain.model.ActivityStatistic
import com.jdevs.timeo.shared.util.getUnitsSinceEpoch
import com.jdevs.timeo.util.time.getHours
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class ChartState(val entries: StatsEntries, val formatter: ValueFormatter) {
    companion object {
        operator fun invoke(
            entries: StatsEntries?, formatter: ValueFormatter
        ) = entries?.let { ChartState(entries, formatter) }
    }
}

fun List<ActivityStatistic>.toEntries(
    timeUnit: ChronoUnit, entriesCount: Int = STATS_ENTRIES
): StatsEntries? {

    if (!this.hasPositiveValues())
        return null

    val statsList = createStatsList(entriesCount, timeUnit)
    return createEntries(statsList)
}

private fun createEntries(statsList: List<ActivityStatistic>): StatsEntries {
    val entries = statsList.getEntries()
    val previousEntries = statsList.getPrevEntries()

    return StatsEntries(entries, previousEntries)
}

private fun List<ActivityStatistic>.hasPositiveValues() =
    this.any { it.time > 0 }

private fun List<ActivityStatistic>.createStatsList(
    entriesCount: Int, timeUnit: ChronoUnit
): List<ActivityStatistic> {

    val unitsSinceEpoch = OffsetDateTime.now().getUnitsSinceEpoch(timeUnit)

    return List(entriesCount) { index ->
        val units = unitsSinceEpoch - index
        val item = find { chartItem -> chartItem.day == units }

        if (item != null && item.time > 0) item else ActivityStatistic(units, 0)
    }
}

private fun List<ActivityStatistic>.getPrevEntries(): List<Entry>? {

    if (this.size > STATS_ENTRIES)
        return createPrevEntries()

    return null
}

private fun List<ActivityStatistic>.createPrevEntries(): List<Entry> {
    return take(STATS_ENTRIES).map(::mapPrevStat)
}

private fun mapPrevStat(item: ActivityStatistic) =
    Entry(item.day.toFloat() + STATS_ENTRIES, getHours(item.time))

private fun List<ActivityStatistic>.getEntries(): List<Entry> {
    return takeLast(STATS_ENTRIES).map { item ->
        Entry(item.day.toFloat(), getHours(item.time))
    }
}

