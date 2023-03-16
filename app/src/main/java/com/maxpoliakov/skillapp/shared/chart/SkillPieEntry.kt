package com.maxpoliakov.skillapp.shared.chart

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

data class SkillPieEntry(
    val skill: Skill,
    val formattedValue: String,
    @get:JvmName("getSkillPieEntryLabel")
    val label: String,
    val count: Long,
) : PieEntry(count.toFloat(), label) {
    val hasPositiveValue get() = count > 0L

    companion object {
        fun create(skill: Skill, count: Long, context: Context): SkillPieEntry {
            val formattedValue = skill.unit
                .mapToUI()
                .toLongString(count, context)

            return SkillPieEntry(
                skill,
                formattedValue,
                context.getLabel(skill.name, formattedValue),
                count,
            )
        }

        private fun Context.getLabel(name: String, formattedValue: String): String {
            return getString(R.string.pie_chart_legend_entry, name, formattedValue)
        }

        inline fun List<Skill>.toSkillPieEntries(
            context: Context,
            count: (Skill) -> Long = Skill::totalCount,
        ): List<SkillPieEntry> {
            return map { skill ->
                create(skill, count(skill), context)
            }
        }
    }
}

