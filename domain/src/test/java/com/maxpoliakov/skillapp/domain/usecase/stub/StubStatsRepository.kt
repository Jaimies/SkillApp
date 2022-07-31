package com.maxpoliakov.skillapp.domain.usecase.stub

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

open class StubStatsRepository : StatsRepository {
    override fun getCountAtDate(id: Id, date: LocalDate) = flowOf(2L)
}
