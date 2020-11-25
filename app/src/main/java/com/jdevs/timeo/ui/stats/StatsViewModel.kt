package com.jdevs.timeo.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.util.charts.toStatsEntries
import com.jdevs.timeo.util.charts.withMissingStats
import kotlinx.coroutines.flow.map

open class StatsViewModel(
    getStats: GetStatsUseCase,
    activityId: Int
) : ViewModel() {

    val stats = getStats.run(activityId).map { stats ->
        stats.withMissingStats().toStatsEntries()
    }.asLiveData()
}
