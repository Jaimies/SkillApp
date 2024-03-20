package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class StubStatsRepository(
    private val stats: Map<Int, List<Statistic>>,
) : StatsRepository {

    override fun getStats(skillId: Id, dateRange: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return flowOf(stats[skillId]!!)
    }

    override fun getStats(skillIds: List<Id>, dateRange: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return flowOf(stats.filterKeys { skillIds.contains(it) }.flatMap { it.value })
    }

    override fun getCount(id: Id, range: ClosedRange<LocalDate>) = flowOf(2L)
    override fun getCount(ids: List<Id>, range: ClosedRange<LocalDate>) = flowOf(2L)

    override suspend fun addRecord(record: Record) {}
}
