package com.maxpoliakov.skillapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.util.charts.toEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import kotlinx.coroutines.flow.map

open class StatsViewModel(
    getStats: GetStatsUseCase,
    skillId: Int
) : ViewModel() {

    protected val stats = getStats.run(skillId)

    val statsChartData = stats.map { stats ->
        stats.withMissingStats().toEntries()
    }.asLiveData()
}
