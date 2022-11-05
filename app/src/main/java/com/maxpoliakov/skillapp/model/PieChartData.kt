package com.maxpoliakov.skillapp.model

import com.github.mikephil.charting.data.PieEntry

data class PieChartData(
    val entries: List<PieEntry>,
    val unit: UiMeasurementUnit,
)
