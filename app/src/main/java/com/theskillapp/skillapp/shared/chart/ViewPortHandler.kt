package com.theskillapp.skillapp.shared.chart

import com.github.mikephil.charting.utils.ViewPortHandler

fun ViewPortHandler.setScaleXRange(range: ClosedFloatingPointRange<Float>) {
    setMaximumScaleX(range.endInclusive)
    setMinimumScaleX(range.start)
}
