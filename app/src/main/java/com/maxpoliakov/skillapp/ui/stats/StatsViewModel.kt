package com.maxpoliakov.skillapp.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.util.charts.toStatsEntries
import com.maxpoliakov.skillapp.util.charts.withMissingStats
import kotlinx.coroutines.flow.map

open class StatsViewModel(
    getStats: GetStatsUseCase,
    activityId: Int
) : ViewModel() {
    val stats = getStats.run(activityId).map { stats ->
        stats.withMissingStats().toStatsEntries()
    }.asLiveData()
}
