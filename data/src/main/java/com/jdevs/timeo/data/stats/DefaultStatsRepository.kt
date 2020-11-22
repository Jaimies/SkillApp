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

    override suspend fun addDayRecord(record: Record) {
        statsDao.addDayRecord(record.activityId, record.timestamp.daysSinceEpoch, record.time.toMinutes())
    }

    override suspend fun addWeekRecord(record: Record) {
        statsDao.addWeekRecord(record.activityId, record.timestamp.weeksSinceEpoch, record.time.toMinutes())
    }

    override suspend fun addMonthRecord(record: Record) {
        statsDao.addMonthRecord(record.activityId, record.timestamp.monthSinceEpoch, record.time.toMinutes())
    }
}
