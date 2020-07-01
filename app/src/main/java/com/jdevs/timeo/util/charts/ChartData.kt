package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.data.stats.STATS_ENTRIES
import com.jdevs.timeo.domain.model.ActivityStatistic
import com.jdevs.timeo.util.time.getHours
import org.threeten.bp.OffsetDateTime
import kotlin.math.ceil

fun List<ActivityStatistic>.toChartData(
    decreaseByUnit: OffsetDateTime.(Long) -> OffsetDateTime,
    getEpochUnits: OffsetDateTime.() -> Int,
    formatter: ValueFormatter,
    entriesCount: Int = STATS_ENTRIES
): ChartData {

    val result = mutableListOf<ActivityStatistic>()

    repeat(entriesCount) { index ->
        val units = OffsetDateTime.now().decreaseByUnit(index.toLong()).getEpochUnits()
        val item = find { chartItem -> chartItem.day == units }

        val itemToAdd = if (item != null && item.time > 0) item else ActivityStatistic(0, units)
        result.add(index, itemToAdd)
    }

    if (result.find { it.time != 0 } == null) {
        return ChartData(null, formatter)
    }

    val finalRes = result.apply { sortBy(ActivityStatistic::day) }.map { item ->
        Entry(item.day.toFloat(), getHours(item.time))
    }

    return ChartData(finalRes, formatter)
}

class ChartData(val entries: List<Entry>?, val formatter: ValueFormatter)

const val HOURS_BREAKPOINT = 4
private const val HOURS_DIVIDER = 3

val List<Entry>.axisMaximum: Float
    get() {
        val maxValue = maxBy { it.y }?.y ?: 0f

        return when {
            maxValue <= HOURS_BREAKPOINT -> ceil(maxValue)
            maxValue % HOURS_DIVIDER == 0f -> maxValue
            else -> ceil(maxValue / HOURS_DIVIDER) * HOURS_DIVIDER
        }
    }
