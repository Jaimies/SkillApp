package com.theskillapp.skillapp.shared.chart

import android.graphics.Canvas
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class MultilineXAxisRenderer(
    viewPortHandler: ViewPortHandler?,
    xAxis: XAxis?,
    trans: Transformer?
) : XAxisRenderer(viewPortHandler, xAxis, trans) {
    constructor(chart: TheBarChart) : this(
        chart.viewPortHandler,
        chart.xAxis,
        chart.getTransformer(YAxis.AxisDependency.LEFT)
    )

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

