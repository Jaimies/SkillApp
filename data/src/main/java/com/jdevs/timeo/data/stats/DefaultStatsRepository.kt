package com.jdevs.timeo.data.stats

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.mapList
import javax.inject.Inject
import javax.inject.Singleton

const val STATS_ENTRIES = 7
const val DAY_STATS_ENTRIES = 14

@Singleton
class DefaultStatsRepository @Inject constructor(
    private val statsDao: StatsDao
) : StatsRepository {

    override val dayStats by lazy {
        statsDao.getDayStats().mapList { it.mapToDomain() }
    }
    override val weekStats by lazy {
        statsDao.getWeekStats().mapList { it.mapToDomain() }
    }
    override val monthStats by lazy {
        statsDao.getMonthStats().mapList { it.mapToDomain() }
    }

    override suspend fun addDayRecord(activityId: Id, time: Int) {
        statsDao.addDayRecord(activityId, time)
    }

    override suspend fun addWeekRecord(activityId: Id, time: Int) {
        statsDao.addWeekRecord(activityId, time)
    }

    override suspend fun addMonthRecord(activityId: Id, time: Int) {
        statsDao.addMonthRecord(activityId, time)
    }
}
