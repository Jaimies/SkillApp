package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.util.PagingConstants.STATS_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface StatsLocalDataSource {

    val dayStats: LiveData<PagedList<DayStats>>
    val weekStats: LiveData<PagedList<WeekStats>>
    val monthStats: LiveData<PagedList<MonthStats>>
}

@Singleton
class RoomStatsDataSource @Inject constructor(
    private val statsDao: StatsDao,
    dayStatsMapper: DBDayStatsMapper,
    weekStatsMapper: DBWeekStatsMapper,
    monthStatsMapper: DBMonthStatsMapper
) : StatsLocalDataSource {

    override val dayStats by lazy {

        statsDao.getDayStats().toLivePagedList(STATS_PAGE_SIZE, dayStatsMapper)
    }

    override val weekStats by lazy {

        statsDao.getWeekStats().toLivePagedList(STATS_PAGE_SIZE, weekStatsMapper)
    }

    override val monthStats by lazy {

        statsDao.getMonthStats().toLivePagedList(STATS_PAGE_SIZE, monthStatsMapper)
    }
}
