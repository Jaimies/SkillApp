package com.jdevs.timeo.util.charts

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.TimeFormatter
import com.jdevs.timeo.util.ui.getColorCompat

private const val LEFT_OFFSET = 1.5f
private const val TOP_OFFSET = 24f
private const val RIGHT_OFFSET = 8f
private const val BOTTOM_OFFSET = 8f

private const val X_AXIS_SPACE_MIN = 0.4f
private const val X_AXIS_SPACE_MAX = 0.4f
private const val X_AXIS_Y_OFFSET = 10f
private const val X_AXIS_TEXT_SIZE = 14f

private const val Y_AXIS_DASHED_LINE_SIZE = 7f
private const val Y_AXIS_LABEL_COUNT = 4
private const val Y_AXIS_GRANULARITY = 3f
private const val Y_AXIS_TEXT_SIZE = 11f

fun LineChart.setup() {

    legend.isEnabled = false
    axisRight.isEnabled = false
    description.isEnabled = false
    setScaleEnabled(false)
    isDragEnabled = false

    setExtraOffsets(LEFT_OFFSET, TOP_OFFSET, RIGHT_OFFSET, BOTTOM_OFFSET)
    setNoDataTextColor(context.getColorCompat(R.color.brown_900))
    setNoDataText(context.getString(R.string.no_data))

    axisLeft.run {

        setLabelCount(Y_AXIS_LABEL_COUNT, true)
        textSize = Y_AXIS_TEXT_SIZE
        valueFormatter = TimeFormatter()
        isGranularityEnabled = true
        granularity = Y_AXIS_GRANULARITY
        axisMinimum = 0f
        enableGridDashedLine(Y_AXIS_DASHED_LINE_SIZE, Y_AXIS_DASHED_LINE_SIZE, 0f)
        setDrawAxisLine(false)
    }

    xAxis.run {

        spaceMin = X_AXIS_SPACE_MIN
        spaceMax = X_AXIS_SPACE_MAX
        textSize = X_AXIS_TEXT_SIZE
        yOffset = X_AXIS_Y_OFFSET
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
    }
}
