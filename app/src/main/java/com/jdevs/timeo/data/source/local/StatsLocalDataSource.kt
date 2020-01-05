package com.jdevs.timeo.data.source.local

import com.jdevs.timeo.data.source.StatsDataSource
import com.jdevs.timeo.util.PagingConstants.STATS_PAGE_SIZE
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("EmptyFunctionBlock")
class StatsLocalDataSource @Inject constructor(
    private val recordsDao: StatsDao
) : StatsDataSource {

    override val dayStats by lazy {

        recordsDao.getDayStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override val weekStats by lazy {

        recordsDao.getWeekStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override val monthStats by lazy {

        recordsDao.getMonthStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override suspend fun updateStats(date: OffsetDateTime, time: Long) {


    }
}
