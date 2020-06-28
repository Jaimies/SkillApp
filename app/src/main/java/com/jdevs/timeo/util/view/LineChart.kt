package com.jdevs.timeo.util.view

import androidx.annotation.ColorRes
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.jdevs.timeo.R
import com.jdevs.timeo.data.stats.STATS_ENTRIES
import com.jdevs.timeo.ui.common.TimeFormatter
import com.jdevs.timeo.util.charts.ChartData
import com.jdevs.timeo.util.charts.HOURS_BREAKPOINT
import com.jdevs.timeo.util.charts.axisMaximum
import com.jdevs.timeo.util.getColorCompat

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 1.5f
private const val LABEL_COUNT = 5
private const val LABEL_COUNT_SMALL = 4
private const val ENTRIES_COUNT = 7
private const val DASHED_LINE_LENGTH = 7f

fun LineChart.setData(data: ChartData?) {

    if (data?.entries == null) {
        this.data = null
        return
    }

    fun createDataSet(entries: List<Entry>?, @ColorRes color: Int, drawValues: Boolean) =
        LineDataSet(entries, "").apply {
            setDrawValues(drawValues)
            valueTextSize = VALUE_TEXT_SIZE
            valueFormatter = TimeFormatter()
            lineWidth = LINE_WIDTH
            this.color = context.getColorCompat(color)
            setDrawCircles(false)
            isHighlightEnabled = false
        }

    val currentDataSet =
        createDataSet(data.entries.takeLast(ENTRIES_COUNT), R.color.black_800, true)

    val dataSets = mutableListOf(currentDataSet)

    if (data.entries.size > STATS_ENTRIES) {

        val prevStats = data.entries.take(ENTRIES_COUNT).apply { forEach { it.x += ENTRIES_COUNT } }

        if (prevStats.find { it.y > 0 } != null) {
            val prevDataSet = createDataSet(prevStats, R.color.black_700, false)
            prevDataSet.enableDashedLine(DASHED_LINE_LENGTH, DASHED_LINE_LENGTH, 0f)
            dataSets.add(prevDataSet)
        }
    }

    this.data = LineData(dataSets as List<ILineDataSet>)

    axisLeft.run {
        axisMaximum = data.entries.axisMaximum
        setLabelCount(if (axisMaximum <= HOURS_BREAKPOINT) LABEL_COUNT else LABEL_COUNT_SMALL, true)
    }

    xAxis.valueFormatter = data.formatter

    notifyDataSetChanged()
    invalidate()
}
