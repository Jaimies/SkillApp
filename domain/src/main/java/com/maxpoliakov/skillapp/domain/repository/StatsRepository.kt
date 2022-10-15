package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Id
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatsRepository {
    fun getCountAtDateFlow(id: Id, date: LocalDate): Flow<Long>
    suspend fun getCountAtDate(id: Id, date: LocalDate): Long
}
