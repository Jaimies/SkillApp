package com.jdevs.timeo.data.stats

import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import javax.inject.Inject
import javax.inject.Singleton

interface StatsLocalDataSource {

    val dayStats: DataSource.Factory<Int, DayStats>
    val weekStats: DataSource.Factory<Int, WeekStats>
    val monthStats: DataSource.Factory<Int, MonthStats>
}

@Singleton
class RoomStatsDataSource @Inject constructor(private val statsDao: StatsDao) :
    StatsLocalDataSource {

    override val dayStats by lazy { statsDao.getDayStats().map(DBDayStats::mapToDomain) }
    override val weekStats by lazy { statsDao.getWeekStats().map(DBWeekStats::mapToDomain) }
    override val monthStats by lazy { statsDao.getMonthStats().map(DBMonthStats::mapToDomain) }
}
