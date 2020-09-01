package com.jdevs.timeo.domain.repository

import com.jdevs.timeo.domain.model.Statistic
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    val dayStats: Flow<List<Statistic>>
    val weekStats: Flow<List<Statistic>>
    val monthStats: Flow<List<Statistic>>
}
