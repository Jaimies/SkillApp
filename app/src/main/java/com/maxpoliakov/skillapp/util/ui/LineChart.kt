package com.maxpoliakov.skillapp.util.ui

import android.graphics.Color.WHITE
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import com.maxpoliakov.skillapp.util.charts.HOURS_BREAKPOINT
import com.maxpoliakov.skillapp.util.charts.StatsEntries

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 1.5f
private const val LABEL_COUNT = 5
private const val LABEL_COUNT_SMALL = 4
private const val DASHED_LINE_LENGTH = 7f

fun LineChart.setState(entries: StatsEntries?) {
    if (entries != null) {
        updateUI(entries)
    } else {
        this.data = null
    }
}

private fun LineChart.updateUI(entries: StatsEntries) {
    this.data = LineData(createDataSets(entries))
    setupAxises(entries)

    notifyDataSetChanged()
    invalidate()
}

private fun createDataSets(entries: StatsEntries): List<LineDataSet> {
    val currentDataSet = createDataSet(entries.entries)
    val previousDataSet = createPreviousDataSet(entries.previousEntries)

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

private fun LineChart.setupAxises(entries: StatsEntries) {
    axisLeft.axisMaximum = entries.getMaximumValue()
    axisLeft.setLabelCount(getLabelCount(axisLeft.axisMaximum), true)
}

private fun getLabelCount(axisMaximum: Float) =
    if (axisMaximum <= HOURS_BREAKPOINT) LABEL_COUNT else LABEL_COUNT_SMALL
