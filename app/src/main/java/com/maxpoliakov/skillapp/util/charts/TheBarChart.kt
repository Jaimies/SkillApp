package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
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
import com.maxpoliakov.skillapp.domain.model.TimeTarget
import com.maxpoliakov.skillapp.ui.common.ChartData
import com.maxpoliakov.skillapp.ui.common.DayFormatter
import com.maxpoliakov.skillapp.ui.common.MonthFormatter
import com.maxpoliakov.skillapp.ui.common.TimeFormatter
import com.maxpoliakov.skillapp.ui.common.WeekFormatter
import com.maxpoliakov.skillapp.util.ui.getPrimaryColor
import com.maxpoliakov.skillapp.util.ui.getTextColor
import com.maxpoliakov.skillapp.util.ui.sp
import java.time.temporal.ChronoUnit

class TheBarChart : BarChart {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private var entries: List<BarEntry>? = null
    private var formatterType: ChronoUnit? = null

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
    }

    private fun updateUI(entries: List<BarEntry>) {
        this.data = BarData(createDataSets(entries))
        this.data.barWidth = 0.3f
        zoom(8f, 1f, entries.last().x, 0f)
        notifyDataSetChanged()
        invalidate()
    }

    private fun createDataSets(entries: List<BarEntry>): List<BarDataSet> {
        return listOf(createDataSet(entries))
    }

    private fun createDataSet(entries: List<BarEntry>): BarDataSet {
        val textColor = context.getTextColor()

        return BarDataSet(entries, "").apply {
            valueTextSize = VALUE_TEXT_SIZE
            valueFormatter = TimeFormatter(context)
            valueTextColor = textColor
            this.color = textColor
            color = context.getPrimaryColor()
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
        setNoDataTextColor(context.getTextColor())
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
            textColor = context.getTextColor()
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

    fun setGoal(goal: TimeTarget) {
        if (!shouldDisplayGoal(goal)) return

        axisLeft.run {
            val limitLine = LimitLine(goal.duration.seconds.toFloat(), "Daily goal").apply {
                lineWidth = 1f
                lineColor = Color.WHITE
                textColor = Color.WHITE
                labelPosition = LimitLabelPosition.LEFT_BOTTOM
                textSize = 12.sp.toDp(context)
                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            }
            removeAllLimitLines()
            addLimitLine(limitLine)

            axisMaximum = (entries?.maxByOrNull { it.y }?.y ?: 0f).coerceAtLeast(goal.duration.seconds.toFloat())
        }
    }

    private fun shouldDisplayGoal(goal: TimeTarget): Boolean {
        return goal.interval == TimeTarget.Interval.Daily && formatterType == ChronoUnit.DAYS
                || goal.interval == TimeTarget.Interval.Weekly && formatterType == ChronoUnit.WEEKS
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
            Utils.drawXAxisValue(
                c,
                line[1], x, y + mAxisLabelPaint.textSize, mAxisLabelPaint, anchor, angleDegrees
            )
        }
    }

    private fun setFormatterType(type: ChronoUnit) {
        if (type == this.formatterType) return

        formatterType = type

        xAxis.valueFormatter = when (type) {
            ChronoUnit.DAYS -> DayFormatter()
            ChronoUnit.WEEKS -> WeekFormatter()
            ChronoUnit.MONTHS -> MonthFormatter()
            else -> return
        }

        if (type == ChronoUnit.DAYS) {
            viewPortHandler.setMaximumScaleX(8f)
            viewPortHandler.setMinimumScaleX(4f)
            setScaleEnabled(true)
            entries?.let { entries -> zoom(8f, 1f, entries.last().x, 0f) }
        } else {
            viewPortHandler.setMaximumScaleX(1f)
            viewPortHandler.setMinimumScaleX(1f)
            setScaleEnabled(false)
        }

        invalidate()
    }

    fun update(type: ChronoUnit, chartData: ChartData, viewLifecycleOwner: LifecycleOwner) {
        val stats = when (type) {
            ChronoUnit.DAYS -> chartData.dailyStats
            ChronoUnit.WEEKS -> chartData.weeklyStats
            ChronoUnit.MONTHS -> chartData.monthlyStats
            else -> return
        }

        chartData.dailyStats.removeObservers(viewLifecycleOwner)
        chartData.weeklyStats.removeObservers(viewLifecycleOwner)
        chartData.monthlyStats.removeObservers(viewLifecycleOwner)

        stats.observe(viewLifecycleOwner) { state ->
            setState(state)
            setFormatterType(type)
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

        private const val Y_AXIS_LABEL_COUNT = 5
        private const val Y_AXIS_GRANULARITY = 3f
    }
}
