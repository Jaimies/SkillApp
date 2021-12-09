package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.data.serialization.LocalDateAsStringSerializer
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.daysAgo
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao
) : StatsRepository {

    override fun getStats(skillId: Id): Flow<List<Statistic>> {
        return statsDao.getStats(skillId).mapList { it.mapToDomain() }
    }

    override fun getStats(skillId: Id, startDate: LocalDate): Flow<List<Statistic>> {
        val daysAgoStart = ChronoUnit.DAYS.between(startDate, LocalDate.now())
        return statsDao.getStats(skillId, daysAgoStart).mapList { it.mapToDomain() }
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.skillId, record.date, record.time)
    }

    override suspend fun getTimeAtDate(date: LocalDate): Duration {
        return statsDao.getTimeAtDate(date)
    }
}
