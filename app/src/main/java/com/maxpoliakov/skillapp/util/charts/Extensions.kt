package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.utils.ViewPortHandler

fun ViewPortHandler.setScaleXRange(range: ClosedFloatingPointRange<Float>) {
    setMaximumScaleX(range.endInclusive)
    setMinimumScaleX(range.start)
}
