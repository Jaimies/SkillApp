package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DBStatsRepository @Inject constructor(
    private val statsDao: StatsDao,
) : StatsRepository {

    override fun getStats(skillId: Id, dateRange: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return getStats(listOf(skillId), dateRange)
    }

    override fun getStats(skillIds: List<Id>, dateRange: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return statsDao.getStatsGroupedByDate(
            skillIds,
            dateRange.start,
            dateRange.endInclusive,
        ).mapList(DBStatistic::mapToDomain)
    }

    override fun getCount(id: Id, range: ClosedRange<LocalDate>): Flow<Long> {
        return this.getCount(listOf(id), range)
    }

    override fun getCount(ids: List<Id>, range: ClosedRange<LocalDate>): Flow<Long> {
        return statsDao.getCountInDateRange(ids, range.start, range.endInclusive).map { time -> time ?: 0 }
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.skillId, record.date, record.count)
    }
}
