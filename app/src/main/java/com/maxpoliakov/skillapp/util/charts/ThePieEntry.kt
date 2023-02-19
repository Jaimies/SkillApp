package com.maxpoliakov.skillapp.util.charts

import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.domain.model.Id

class ThePieEntry(
    val skillId: Id,
    val name: String,
    val formattedValue: String,
    label: String,
    value: Float,
) : PieEntry(value, label)
