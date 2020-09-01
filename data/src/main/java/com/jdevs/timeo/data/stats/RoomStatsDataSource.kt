package com.jdevs.timeo.data.stats

import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface StatsDataSource {
    val dayStats: Flow<List<Statistic>>
    val weekStats: Flow<List<Statistic>>
    val monthStats: Flow<List<Statistic>>
}

@Singleton
class RoomStatsDataSource @Inject constructor(private val statsDao: StatsDao) :
    StatsDataSource {

    override val dayStats by lazy {
        statsDao.getDayStats().mapList { it.mapToDomain() }
    }
    override val weekStats by lazy {
        statsDao.getWeekStats().mapList { it.mapToDomain() }
    }
    override val monthStats by lazy {
        statsDao.getMonthStats().mapList { it.mapToDomain() }
    }
}
