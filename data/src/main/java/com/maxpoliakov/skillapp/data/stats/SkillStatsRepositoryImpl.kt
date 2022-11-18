package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillStatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao,
) : SkillStatsRepository {

    override fun getStats(skillId: Id, dateRange: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return statsDao.getStats(
            skillId,
            dateRange.start,
            dateRange.endInclusive,
        ).mapList(DBStatistic::mapToDomain)
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.skillId, record.date, record.count)
    }

    override fun getCountAtDateFlow(id: Id, date: LocalDate): Flow<Long> {
        return statsDao.getCountAtDateFlow(id, date).map { time -> time ?: 0 }
    }

    override suspend fun getCount(id: Id, range: ClosedRange<LocalDate>): Long {
        return statsDao.getCountInDateRange(id, range.start, range.endInclusive) ?: 0
    }
}
