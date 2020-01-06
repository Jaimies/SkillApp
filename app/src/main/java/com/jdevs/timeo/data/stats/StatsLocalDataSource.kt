package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import com.jdevs.timeo.data.db.StatsDao
import com.jdevs.timeo.data.db.toLivePagedList
import com.jdevs.timeo.util.PagingConstants.STATS_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

interface StatsDataSource {

    val dayStats: LiveData<*>?
    val weekStats: LiveData<*>?
    val monthStats: LiveData<*>?
}

@Singleton
@Suppress("EmptyFunctionBlock")
class StatsLocalDataSourceImpl @Inject constructor(
    private val statsDao: StatsDao
) : StatsDataSource {

    override val dayStats by lazy {

        statsDao.getDayStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override val weekStats by lazy {

        statsDao.getWeekStats().toLivePagedList(STATS_PAGE_SIZE)
    }

    override val monthStats by lazy {

        statsDao.getMonthStats().toLivePagedList(STATS_PAGE_SIZE)
    }
}
