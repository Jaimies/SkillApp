package com.jdevs.timeo.domain.repository

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Statistic
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    val dayStats: Flow<List<Statistic>>
    val weekStats: Flow<List<Statistic>>
    val monthStats: Flow<List<Statistic>>

    suspend fun addDayRecord(activityId: Id, time: Int)
    suspend fun addWeekRecord(activityId: Id, time: Int)
    suspend fun addMonthRecord(activityId: Id, time: Int)
}
