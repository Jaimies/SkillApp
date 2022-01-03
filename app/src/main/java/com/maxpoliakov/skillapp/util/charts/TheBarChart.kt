package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.LifecycleOwner
import com.github.mikephil.charting.charts.BarChart
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
        val textColor = context.getTextColor()

        axisLeft.run {
            setLabelCount(Y_AXIS_LABEL_COUNT, true)
            valueFormatter = TimeFormatter(context)
            isGranularityEnabled = true
            granularity = Y_AXIS_GRANULARITY
            this.textColor = textColor
            axisMinimum = 0f
            gridColor = ColorUtils.setAlphaComponent(textColor, 0x30)
            gridLineWidth = 1.5f
            setDrawAxisLine(false)
        }
    }

    private fun disableUnneededFeatures() {
        legend.isEnabled = false
        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        description.isEnabled = false
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

    fun setFormatterType(type: FormatterType) {
        xAxis.valueFormatter = when (type) {
            FormatterType.Daily -> DayFormatter()
            FormatterType.Weekly -> WeekFormatter()
            FormatterType.Monthly -> MonthFormatter()
        }

        if (type == FormatterType.Daily) {
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

        stats.observe(viewLifecycleOwner, this::setState)
    }

    enum class FormatterType { Daily, Weekly, Monthly }

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
