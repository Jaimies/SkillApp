package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.WeekStats
import org.threeten.bp.OffsetDateTime

interface StatsRepository {

    val dayStats: LiveData<PagedList<DayStats>>
    val dayStatsRemote: List<LiveData<Operation>>
    val weekStats: LiveData<PagedList<WeekStats>>
    val weekStatsRemote: List<LiveData<Operation>>
    val monthStats: LiveData<PagedList<MonthStats>>
    val monthStatsRemote: List<LiveData<Operation>>

    suspend fun updateStats(date: OffsetDateTime, time: Long)
}
