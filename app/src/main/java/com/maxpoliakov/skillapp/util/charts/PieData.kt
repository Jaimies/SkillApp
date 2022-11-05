package com.maxpoliakov.skillapp.util.charts

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PieData(
    val skills: Flow<List<Skill>>,
    val unit: Flow<MeasurementUnit>,
) {
    constructor(group: Flow<SkillGroup>)
            : this(group.map { it.skills }, group.map { it.unit })

    val data = combine(skills, unit) { skills, unit ->
        val entries = skills.map { skill ->
            PieEntry(skill.totalCount.toFloat(), skill.name)
        }

        PieChartData(entries, unit.mapToUI())
    }.asLiveData()

    val isEmpty = data.map { it.entries.isEmpty() }
}
