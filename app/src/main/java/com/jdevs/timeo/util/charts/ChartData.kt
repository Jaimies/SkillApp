package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.domain.model.ActivityDayStats
import com.jdevs.timeo.domain.model.ActivityMonthStats
import com.jdevs.timeo.domain.model.ActivityWeekStats
import com.jdevs.timeo.shared.util.WEEK_DAYS
import com.jdevs.timeo.util.time.getHours
import org.threeten.bp.OffsetDateTime
import kotlin.math.ceil

inline fun List<ChartItem>.toChartData(
    crossinline decreaseByUnit: OffsetDateTime.(Long) -> OffsetDateTime,
    crossinline getEpochUnits: OffsetDateTime.() -> Int,
    formatter: ValueFormatter
): ChartData {

    val result = mutableListOf<ChartItem>()

    for (index in 0 until WEEK_DAYS) {

        val units = OffsetDateTime.now().decreaseByUnit(index.toLong()).getEpochUnits()
        val item = find { chartItem -> chartItem.period == units }

        val itemToAdd = if (item != null && item.time > 0) item else ChartItem(units, 0)
        result.add(index, itemToAdd)
    }

    if (result.find { it.time != 0 } == null) {

        return ChartData(null, formatter)
    }

    val finalRes = result.apply { sortBy(ChartItem::period) }.map { item ->

        Entry(item.period.toFloat(), getHours(item.time))
    }

    return ChartData(finalRes, formatter)
}

data class ChartItem(val period: Int, val time: Int)
class ChartData(val entries: List<Entry>?, val formatter: ValueFormatter)

fun ActivityDayStats.toChartItem() = ChartItem(day, time)
fun ActivityWeekStats.toChartItem() = ChartItem(week, time)
fun ActivityMonthStats.toChartItem() = ChartItem(month, time)

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
