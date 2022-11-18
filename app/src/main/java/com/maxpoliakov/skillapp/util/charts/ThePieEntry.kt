package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.PieEntry

class ThePieEntry(
    val name: String,
    val formattedValue: String,
    label: String,
    value: Float,
) : PieEntry(value, label)
