package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import com.maxpoliakov.skillapp.model.UiStatisticInterval.Companion.mapToUI
import com.maxpoliakov.skillapp.ui.common.DayFormatter
import com.maxpoliakov.skillapp.util.ui.primaryColor
import com.maxpoliakov.skillapp.util.ui.sp
import com.maxpoliakov.skillapp.util.ui.textColor

class TheBarChart : BarChart {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private var entries: List<BarEntry>? = null
    private var intervalType: UiStatisticInterval? = null
    private var unit = UiMeasurementUnit.Millis
    private var goal: UiGoal? = null

    init {
        setup()
    }

    fun setState(entries: List<BarEntry>?) {
        if (entries != null) {
            this.entries = entries
            updateUI(entries)
        } else {
            this.entries = null
            this.data = null
        }

        notifyDataSetChanged()
        invalidate()
    }

    private fun updateUI(entries: List<BarEntry>) {
        this.data = BarData(createDataSets(entries))
        this.data.barWidth = 0.3f
        zoom(8f, 1f, entries.last().x, 0f)
        updateAxisMaximum()
    }

    private fun createDataSets(entries: List<BarEntry>): List<BarDataSet> {
        return listOf(createDataSet(entries))
    }

    private fun createDataSet(entries: List<BarEntry>): BarDataSet {
        val textColor = context.textColor

        return BarDataSet(entries, "").apply {
            valueTextSize = VALUE_TEXT_SIZE
            valueFormatter = unit.getValueFormatter(context)
            valueTextColor = textColor
            this.color = textColor
            color = context.primaryColor
            isHighlightEnabled = false
        }
    }

    private fun setup() {
        disableUnneededFeatures()
        setupOffsets()
        setupFonts()
        setupAxes()
        setupZoom()
        setupNoDataText()
    }

    private fun setupZoom() {
        viewPortHandler.setMaximumScaleY(1f)
        setXAxisRenderer(
            CustomXAxisRenderer(
                viewPortHandler,
                xAxis,
                getTransformer(YAxis.AxisDependency.LEFT)
            )
        )
    }

    private fun setupOffsets() {
        setExtraOffsets(LEFT_OFFSET, TOP_OFFSET, RIGHT_OFFSET, BOTTOM_OFFSET)
    }

    private fun setupNoDataText() {
        val textSize = 14f.sp.toPx(context)
        getPaint(PAINT_INFO).textSize = textSize.toFloat()
        setNoDataTextColor(context.textColor)
        setNoDataText(context.getString(R.string.no_stats))
    }

    private fun setupFonts() {
        val textSize = 12.5f.sp.toDp(context)
        axisLeft.textSize = textSize
        axisLeft.xOffset = 7f
        xAxis.textSize = textSize
    }

    private fun setupAxes() {
        setupXAxis()
        setupLeftAxis()
    }

    private fun setupXAxis() {
        xAxis.run {
            valueFormatter = DayFormatter()
            spaceMin = X_AXIS_SPACE_MIN
            spaceMax = X_AXIS_SPACE_MAX
            yOffset = X_AXIS_Y_OFFSET
            textColor = context.textColor
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
        }
    }

    private fun setupLeftAxis() {
        axisLeft.run {
            axisMinimum = 0f
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(false)
        }
    }

    private fun disableUnneededFeatures() {
        legend.isEnabled = false
        axisRight.isEnabled = false
        description.isEnabled = false
    }

    fun setGoal(goal: UiGoal) {
        this.goal = goal
        displayGoal(goal)
    }

    private fun displayGoal(goal: UiGoal?) {
        if (goal == null || !shouldDisplayGoal(goal)) {
            axisLeft.removeAllLimitLines()
            axisLeft.resetAxisMaximum()
            notifyDataSetChanged()
            invalidate()
            return
        }

        axisLeft.run {
            removeAllLimitLines()
            val label = goal.unit.toLongString(goal.count, context)
            val limitLine = LimitLine(goal.count.toFloat(), label).apply {
                val color = context.textColor
                lineWidth = 1f
                lineColor = color
                textColor = color
                labelPosition = LimitLabelPosition.LEFT_BOTTOM
                textSize = 12.sp.toDp(context)
                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            }
            addLimitLine(limitLine)

            updateAxisMaximum()
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun updateAxisMaximum() {
        val goal = this.goal

        if (goal == null) {
            axisLeft.resetAxisMaximum()
        } else {
            val axisMaximum = (entries?.maxByOrNull { it.y }?.y ?: 0f) * 1.1f
            val goalTime = goal.count.toFloat()

            if (goalTime < axisMaximum) {
                axisLeft.resetAxisMaximum()
            } else {
                axisLeft.axisMaximum = goal.count.toFloat().coerceAtLeast(axisMaximum)
            }
        }
    }

    private fun shouldDisplayGoal(goal: UiGoal): Boolean {
        return goal.type.toDomain().interval.mapToUI() == intervalType
    }

    class CustomXAxisRenderer(viewPortHandler: ViewPortHandler?, xAxis: XAxis?, trans: Transformer?) :
        XAxisRenderer(viewPortHandler, xAxis, trans) {
        override fun drawLabel(
            c: Canvas?,
            formattedLabel: String,
            x: Float,
            y: Float,
            anchor: MPPointF?,
            angleDegrees: Float
        ) {
            val line = formattedLabel.split("\n").toTypedArray()
            Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees)

            if (line.size == 1) return

            Utils.drawXAxisValue(
                c,
                line[1], x, y + mAxisLabelPaint.textSize, mAxisLabelPaint, anchor, angleDegrees
            )
        }
    }

    private fun setFormatterType(interval: UiStatisticInterval) {
        if (interval == this.intervalType) return

        intervalType = interval

        xAxis.valueFormatter = interval.valueFormatter

        viewPortHandler.setMaximumScaleX(interval.maxScale)
        viewPortHandler.setMinimumScaleX(interval.minScale)
        setScaleEnabled(interval.scaleEnabled)
        entries?.let { entries -> zoom(interval.maxScale, 1f, entries.last().x, 0f) }

        displayGoal(goal)
        invalidate()
    }

    fun update(data: BarChartData) {
        setState(data.entries)
        setFormatterType(data.interval)
    }

    fun setUnit(unit: UiMeasurementUnit) {
        this.unit = unit
        val valueFormatter = unit.getValueFormatter(context)
        data?.dataSets?.forEach { dataset ->
            dataset.valueFormatter = valueFormatter
        }
    }

    companion object {
        private const val VALUE_TEXT_SIZE = 12f

        private const val LEFT_OFFSET = 15f
        private const val TOP_OFFSET = 27f
        private const val RIGHT_OFFSET = 20f
        private const val BOTTOM_OFFSET = 60f

        private const val X_AXIS_SPACE_MIN = 0.4f
        private const val X_AXIS_SPACE_MAX = 0.4f
        private const val X_AXIS_Y_OFFSET = 10f
    }
}
