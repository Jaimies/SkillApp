package com.jdevs.timeo.util.view

import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jdevs.timeo.R
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

@BindingAdapter("data")
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

    val previousDataSet = createDataSet(
        data.entries.take(ENTRIES_COUNT).apply { forEach { it.x += ENTRIES_COUNT } },
        R.color.black_700,
        false
    )

    previousDataSet.enableDashedLine(7f, 7f, 0f)

    this.data = LineData(currentDataSet, previousDataSet)

    axisLeft.run {
        axisMaximum = data.entries.axisMaximum
        setLabelCount(if (axisMaximum <= HOURS_BREAKPOINT) LABEL_COUNT else LABEL_COUNT_SMALL, true)
    }

    xAxis.valueFormatter = data.formatter

    notifyDataSetChanged()
    invalidate()
}
