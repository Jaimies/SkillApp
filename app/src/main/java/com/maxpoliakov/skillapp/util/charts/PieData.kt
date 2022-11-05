package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PieData @AssistedInject constructor(
    @ApplicationContext
    context: Context,
    @Assisted
    val skills: Flow<List<Skill>>,
) {
    val data = skills.map { skills ->
        val entries = skills.map { skill ->
            PieEntry(skill.totalCount.toFloat(), context.getLabel(skill))
        }

        PieChartData(entries)
    }.asLiveData()

    private fun Context.getLabel(skill: Skill) = getString(
        R.string.pie_chart_legend_entry,
        skill.name,
        skill.unit.mapToUI().toLongString(skill.totalCount, this)
    )

    val isEmpty = data.map { it.entries.isEmpty() }

    @AssistedFactory
    interface Factory {
        fun create(skills: Flow<List<Skill>>): PieData
    }
}
