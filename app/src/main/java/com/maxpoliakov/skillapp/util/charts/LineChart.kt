package com.maxpoliakov.skillapp.util.charts

import androidx.core.graphics.ColorUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import com.maxpoliakov.skillapp.ui.common.WeekDayFormatter
import com.maxpoliakov.skillapp.util.ui.getTextColor

private const val LEFT_OFFSET = 1.5f
private const val TOP_OFFSET = 24f
private const val RIGHT_OFFSET = 8f
private const val BOTTOM_OFFSET = 8f

private const val X_AXIS_SPACE_MIN = 0.4f
private const val X_AXIS_SPACE_MAX = 0.4f
private const val X_AXIS_Y_OFFSET = 10f

private const val Y_AXIS_LABEL_COUNT = 5
private const val Y_AXIS_GRANULARITY = 3f

private const val TEXT_SIZE = 11F

fun LineChart.setup() {
    disableUnneededFeatures()
    setupOffsets()
    setupLeftAxis()
    setupXAxis()
    setupNoDataText()
}

private fun LineChart.setupOffsets() {
    setExtraOffsets(LEFT_OFFSET, TOP_OFFSET, RIGHT_OFFSET, BOTTOM_OFFSET)
}

private fun LineChart.setupNoDataText() {
    setNoDataTextColor(context.getTextColor())
    setNoDataText(context.getString(R.string.no_data))
}

private fun LineChart.setupXAxis() {
    xAxis.run {
        valueFormatter = WeekDayFormatter()
        spaceMin = X_AXIS_SPACE_MIN
        spaceMax = X_AXIS_SPACE_MAX
        textSize = TEXT_SIZE
        yOffset = X_AXIS_Y_OFFSET
        textColor = context.getTextColor()
        position = BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(false)
    }
}

private fun LineChart.setupLeftAxis() {
    val textColor = context.getTextColor()

    axisLeft.run {
        setLabelCount(Y_AXIS_LABEL_COUNT, true)
        textSize = TEXT_SIZE
        valueFormatter = TimeFormatter()
        isGranularityEnabled = true
        granularity = Y_AXIS_GRANULARITY
        this.textColor = textColor
        axisMinimum = 0f
        gridColor = ColorUtils.setAlphaComponent(textColor, 0x30)
        gridLineWidth = 1.5f
        setDrawAxisLine(false)
    }
}

private fun LineChart.disableUnneededFeatures() {
    legend.isEnabled = false
    axisRight.isEnabled = false
    description.isEnabled = false
    setScaleEnabled(false)
    isDragEnabled = false
}
