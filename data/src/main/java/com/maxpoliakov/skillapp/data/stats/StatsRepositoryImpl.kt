package com.maxpoliakov.skillapp.data.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao
) : StatsRepository {

    override fun getStats(skillId: Id): Flow<List<Statistic>> {
        return statsDao.getStats(skillId).mapList { it.mapToDomain() }
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.skillId, record.date, record.time)
    }

    override suspend fun getTimeAtDate(date: LocalDate): Duration {
        return statsDao.getTimeAtDate(date)
    }
}
