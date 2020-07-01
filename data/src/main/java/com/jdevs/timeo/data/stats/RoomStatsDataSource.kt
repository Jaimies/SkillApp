package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.shared.util.mapList
import javax.inject.Inject
import javax.inject.Singleton

interface StatsDataSource {

    val dayStats: LiveData<List<Statistic>>
    val weekStats: LiveData<List<Statistic>>
    val monthStats: LiveData<List<Statistic>>
}

@Singleton
class RoomStatsDataSource @Inject constructor(private val statsDao: StatsDao) : StatsDataSource {

    override val dayStats by lazy { statsDao.getDayStats().mapList(DBDayStats::mapToDomain) }
    override val weekStats by lazy { statsDao.getWeekStats().mapList(DBWeekStats::mapToDomain) }
    override val monthStats by lazy { statsDao.getMonthStats().mapList(DBMonthStats::mapToDomain) }
}
