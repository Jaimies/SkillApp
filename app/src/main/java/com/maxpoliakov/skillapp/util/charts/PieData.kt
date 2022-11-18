package com.maxpoliakov.skillapp.util.charts

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.util.charts.SkillPieEntry.Companion.toPieEntries
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PieData @AssistedInject constructor(
    @ApplicationContext
    private val context: Context,
    @Assisted
    val skills: Flow<List<SkillPieEntry>>,
) {
    val data = skills.map { skills ->
        PieChartData(skills.toPieEntries(context))
    }.asLiveData()

    val isEmpty = data.map { it.entries.isEmpty() }

    @AssistedFactory
    interface Factory {
        fun create(skills: Flow<List<SkillPieEntry>>): PieData
    }
}
