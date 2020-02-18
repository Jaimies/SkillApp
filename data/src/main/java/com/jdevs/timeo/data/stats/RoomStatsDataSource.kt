package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.shared.util.map
import javax.inject.Inject
import javax.inject.Singleton

interface StatsDataSource {

    val dayStats: LiveData<List<DayStats>>
    val weekStats: LiveData<List<WeekStats>>
    val monthStats: LiveData<List<MonthStats>>
}

@Singleton
class RoomStatsDataSource @Inject constructor(private val statsDao: StatsDao) : StatsDataSource {

    override val dayStats by lazy { map(statsDao.getDayStats(), DBDayStats::mapToDomain) }
    override val weekStats by lazy { map(statsDao.getWeekStats(), DBWeekStats::mapToDomain) }
    override val monthStats by lazy { map(statsDao.getMonthStats(), DBMonthStats::mapToDomain) }
}
