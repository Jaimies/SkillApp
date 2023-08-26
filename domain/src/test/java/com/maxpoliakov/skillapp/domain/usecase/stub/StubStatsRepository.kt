package com.maxpoliakov.skillapp.domain.usecase.stub

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class StubStatsRepository(
    private val stats: Map<Int, List<Statistic>>,
) : StatsRepository {

    override fun getStats(skillId: Id, dateRange: ClosedRange<LocalDate>): Flow<List<Statistic>> {
        return flowOf(stats[skillId]!!)
    }

    override fun getCount(id: Id, range: ClosedRange<LocalDate>) = flowOf(2L)
    override fun getCount(ids: List<Id>, range: ClosedRange<LocalDate>) = flowOf(2L)

    override suspend fun addRecord(record: Record) {}
}
