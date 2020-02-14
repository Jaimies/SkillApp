package com.jdevs.timeo.util

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.shared.time.WEEK_DAYS
import com.jdevs.timeo.util.time.getHours
import org.threeten.bp.OffsetDateTime

inline fun List<ChartItem>.toChartData(
    crossinline decreaseByUnit: OffsetDateTime.(Long) -> OffsetDateTime,
    crossinline getEpochUnits: OffsetDateTime.() -> Int,
    formatter: ValueFormatter
): ChartData {

    val result = mutableListOf<ChartItem>()

    for (index in 0 until WEEK_DAYS) {

        val units = OffsetDateTime.now().decreaseByUnit(index.toLong()).getEpochUnits()

        if (count { it.period == units } == 0) {

            result.add(index, ChartItem(units, 0))
        } else {

            val statsItem = single { it.period == units }
            result.add(index, statsItem)
        }
    }

    if (result.count { it.time != 0 } == 0) {

        return ChartData(null, formatter)
    }

    result.sortBy(ChartItem::period)

    val finalRes = result.map { statistic ->

        Entry(statistic.period.toFloat(), getHours(statistic.time).toFloat())
    }

    return ChartData(finalRes, formatter)
}

data class ChartItem(val period: Int, val time: Int)

fun DayStats.mapToPresentation() = ChartItem(day, time)
fun WeekStats.mapToPresentation() = ChartItem(week, time)
fun MonthStats.mapToPresentation() = ChartItem(month, time)

class ChartData(val items: List<Entry>?, val formatter: ValueFormatter)
