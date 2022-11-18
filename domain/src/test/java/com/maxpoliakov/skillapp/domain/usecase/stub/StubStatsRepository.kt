package com.maxpoliakov.skillapp.domain.usecase.stub

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

open class StubStatsRepository : StatsRepository {
    override fun getCountAtDateFlow(id: Id, date: LocalDate) = flowOf(2L)
    override suspend fun getCount(id: Id, range: ClosedRange<LocalDate>) = 2L
}
