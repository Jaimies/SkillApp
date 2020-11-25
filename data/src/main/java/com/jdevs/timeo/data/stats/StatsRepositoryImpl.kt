package com.jdevs.timeo.data.stats

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.Statistic
import com.jdevs.timeo.domain.repository.StatsRepository
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val STATS_ENTRIES = 7
const val DAY_STATS_ENTRIES = 14

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao
) : StatsRepository {

    override fun getStats(activityId: Id): Flow<List<Statistic>> {
        return statsDao.getStats(activityId).mapList { it.mapToDomain() }
    }

    override suspend fun addRecord(record: Record) {
        statsDao.addRecord(record.activityId, record.timestamp.daysSinceEpoch, record.time.toMinutes())
    }
}
