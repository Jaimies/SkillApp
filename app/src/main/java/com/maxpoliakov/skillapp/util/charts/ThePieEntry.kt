package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

class ThePieEntry(
    val name: String,
    val formattedValue: String,
    label: String,
    value: Float,
) : PieEntry(value, label) {
    companion object {
        fun from(skill: Skill, context: Context): ThePieEntry {
            val formattedValue = skill.unit.mapToUI().toLongString(skill.totalCount, context)

            return ThePieEntry(
                skill.name,
                formattedValue,
                context.getLabel(skill.name, formattedValue),
                skill.totalCount.toFloat(),
            )
        }

        private fun Context.getLabel(name: String, formattedValue: String): String {
            return getString(R.string.pie_chart_legend_entry, name, formattedValue)
        }
    }
}
