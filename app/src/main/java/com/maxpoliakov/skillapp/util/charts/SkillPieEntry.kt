package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

data class SkillPieEntry(
    val skill: Skill,
    val count: Long,
) : PieEntry(count.toFloat()) {

    val hasPositiveValue get() = count > 0L

    fun toPieEntry(context: Context): ThePieEntry {
        val formattedValue = skill.unit
            .mapToUI()
            .toLongString(count, context)

        return ThePieEntry(
            skill.id,
            skill.name,
            formattedValue,
            context.getLabel(skill.name, formattedValue),
            count.toFloat(),
        )
    }

    private fun Context.getLabel(name: String, formattedValue: String): String {
        return getString(R.string.pie_chart_legend_entry, name, formattedValue)
    }

    companion object {
        inline fun List<Skill>.toEntries(count: (Skill) -> Long = Skill::totalCount): List<SkillPieEntry> {
            return map { skill ->
                SkillPieEntry(skill, count(skill))
            }
        }

        fun List<SkillPieEntry>.toPieEntries(context: Context): List<ThePieEntry> {
            return map { entry -> entry.toPieEntry(context) }
        }
    }
}

