package com.jdevs.timeo.data.stats

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.domain.model.MonthStatistic
import com.jdevs.timeo.domain.model.WeekStatistic
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.shared.util.monthSinceEpoch
import com.jdevs.timeo.shared.util.weeksSinceEpoch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val STATS_ENTRIES = 7
const val DAY_STATS_ENTRIES = 14

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao
) : StatsRepository {

    override fun getDayStats(activityId: Id): Flow<List<DayStatistic>> {
        return statsDao.getDayStats(activityId).mapList { it.mapToDomain() }
    }

    override fun getWeekStats(activityId: Id): Flow<List<WeekStatistic>> {
        return statsDao.getWeekStats(activityId).mapList { it.mapToDomain() }
    }

    override fun getMonthStats(activityId: Id): Flow<List<MonthStatistic>> {
        return statsDao.getMonthStats(activityId).mapList { it.mapToDomain() }
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
