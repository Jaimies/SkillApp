package com.jdevs.timeo.util.ui

import android.graphics.Color.WHITE
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jdevs.timeo.ui.common.TimeFormatter
import com.jdevs.timeo.util.charts.ChartState
import com.jdevs.timeo.util.charts.HOURS_BREAKPOINT

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 1.5f
private const val LABEL_COUNT = 5
private const val LABEL_COUNT_SMALL = 4
private const val DASHED_LINE_LENGTH = 7f

fun LineChart.setState(state: ChartState?) {
    if (state != null) {
        updateUI(state)
    } else {
        this.data = null
    }
}

private fun LineChart.updateUI(state: ChartState) {
    this.data = LineData(createDataSets(state))
    setupAxises(state)

    notifyDataSetChanged()
    invalidate()
}

private fun createDataSets(data: ChartState): List<LineDataSet> {
    val currentDataSet = createDataSet(data.entries.entries)
    val previousDataSet = createPreviousDataSet(data.entries.previousEntries)

    return listOfNotNull(currentDataSet, previousDataSet)
}

private fun createPreviousDataSet(entries: List<Entry>?): LineDataSet? {
    return entries?.let(::createSecondaryDataSet)
}

private fun createSecondaryDataSet(entries: List<Entry>): LineDataSet {
    return createDataSet(entries).apply {
        enableDashedLine(DASHED_LINE_LENGTH, DASHED_LINE_LENGTH, 0f)
        setDrawValues(false)
    }
}

private fun createDataSet(entries: List<Entry>?): LineDataSet {
    return LineDataSet(entries, "").apply {
        valueTextSize = VALUE_TEXT_SIZE
        valueFormatter = TimeFormatter()
        valueTextColor = WHITE
        this.color = WHITE
        lineWidth = LINE_WIDTH
        setDrawCircles(false)
        isHighlightEnabled = false
    }
}

private fun LineChart.setupAxises(state: ChartState) {
    axisLeft.axisMaximum = state.entries.getMaximumValue()
    axisLeft.setLabelCount(getLabelCount(axisLeft.axisMaximum), true)

    xAxis.valueFormatter = state.formatter
}

private fun getLabelCount(axisMaximum: Float) =
    if (axisMaximum <= HOURS_BREAKPOINT) LABEL_COUNT else LABEL_COUNT_SMALL
