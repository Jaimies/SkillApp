package com.jdevs.timeo.domain.repository

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.DayStatistic
import com.jdevs.timeo.domain.model.MonthStatistic
import com.jdevs.timeo.domain.model.WeekStatistic
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun getDayStats(activityId: Id): Flow<List<DayStatistic>>
    fun getWeekStats(activityId: Id): Flow<List<WeekStatistic>>
    fun getMonthStats(activityId: Id): Flow<List<MonthStatistic>>

    suspend fun addDayRecord(record: Record)
    suspend fun addWeekRecord(record: Record)
    suspend fun addMonthRecord(record: Record)
}
