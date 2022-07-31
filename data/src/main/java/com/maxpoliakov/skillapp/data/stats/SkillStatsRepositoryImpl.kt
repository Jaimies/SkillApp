package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillStatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao,
) : SkillStatsRepository {

    override fun getStats(skillId: Id, startDate: LocalDate): Flow<List<Statistic>> {
        val daysAgoStart = ChronoUnit.DAYS.between(startDate, LocalDate.now())
        return statsDao.getStats(skillId, daysAgoStart).mapList { it.mapToDomain() }
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.skillId, record.date, record.count)
    }

    override fun getCountAtDate(id: Id, date: LocalDate): Flow<Long> {
        return statsDao.getCountAtDate(id, date).map { time -> time ?: 0 }
    }
}
