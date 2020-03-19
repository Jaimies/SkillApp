package com.jdevs.timeo.util.charts

import android.content.Context
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.TimeFormatter
import com.jdevs.timeo.util.getColorCompat

private const val LEFT_OFFSET = 3.2f
private const val BOTTOM_OFFSET = 8f
private const val DEFAULT_OFFSET = 8f

private const val X_AXIS_SPACE_MIN = 0.35f
private const val X_AXIS_SPACE_MAX = 0.7f
private const val X_AXIS_Y_OFFSET = 10f

private const val Y_AXIS_SPACE_TOP = 20f
private const val Y_AXIS_LABEL_COUNT = 5
private const val Y_AXIS_GRANULARITY = 0.1f
private const val Y_AXIS_X_OFFSET = 8f

private const val CHART_TEXT_SIZE = 14f

fun LineChart.setup() {

    legend.isEnabled = false
    axisRight.isEnabled = false
    description.isEnabled = false
    setScaleEnabled(false)
    isDragEnabled = false

    setExtraOffsets(LEFT_OFFSET, DEFAULT_OFFSET, DEFAULT_OFFSET, BOTTOM_OFFSET)
    setNoDataTextColor(context.getColorCompat(R.color.colorTextPrimary))
    setNoDataText(context.getString(R.string.no_data))

    axisLeft.apply {

        spaceTop = Y_AXIS_SPACE_TOP
        labelCount = Y_AXIS_LABEL_COUNT
        textSize = CHART_TEXT_SIZE
        valueFormatter = TimeFormatter()
        isGranularityEnabled = true
        granularity = Y_AXIS_GRANULARITY
        xOffset = Y_AXIS_X_OFFSET
        axisMinimum = 0f
        setDrawAxisLine(false)
        setDrawZeroLine(false)
    }

    xAxis.apply {

        spaceMin = X_AXIS_SPACE_MIN
        spaceMax = X_AXIS_SPACE_MAX
        textSize = CHART_TEXT_SIZE
        yOffset = X_AXIS_Y_OFFSET
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(false)
    }
}

private const val VALUE_TEXT_SIZE = 14f
private const val LINE_WIDTH = 1.7f

fun Context.createLineData(entries: List<Entry>): LineData {

    val dataSet = LineDataSet(entries, "").apply {

        valueTextSize = VALUE_TEXT_SIZE
        valueFormatter = TimeFormatter()
        lineWidth = LINE_WIDTH
        color = getColorCompat(R.color.colorTextPrimaryDark)
        setDrawCircles(false)
        isHighlightEnabled = false
    }

    return LineData(dataSet)
}
