package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.WeekStats
import org.threeten.bp.OffsetDateTime

interface StatsRepository {

    val dayStats: DataSource.Factory<Int, DayStats>
    val dayStatsRemote: List<LiveData<Operation<DayStats>>>
    val weekStats: DataSource.Factory<Int, WeekStats>
    val weekStatsRemote: List<LiveData<Operation<WeekStats>>>
    val monthStats: DataSource.Factory<Int, MonthStats>
    val monthStatsRemote: List<LiveData<Operation<MonthStats>>>

    suspend fun updateStats(date: OffsetDateTime, time: Long)
}
