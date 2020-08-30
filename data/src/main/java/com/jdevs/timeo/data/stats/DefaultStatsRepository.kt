package com.jdevs.timeo.data.stats

import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject
import javax.inject.Singleton

const val STATS_ENTRIES = 7
const val DAY_STATS_ENTRIES = 14

@Singleton
class DefaultStatsRepository @Inject constructor(
    private val dataSource: StatsDataSource
) : StatsRepository {

    override val dayStats get() = dataSource.dayStats
    override val weekStats get() = dataSource.weekStats
    override val monthStats get() = dataSource.monthStats
}
