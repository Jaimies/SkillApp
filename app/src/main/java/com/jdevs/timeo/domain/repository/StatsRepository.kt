package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import org.threeten.bp.OffsetDateTime

interface StatsRepository {

    val dayStats: LiveData<*>?
    val weekStats: LiveData<*>?
    val monthStats: LiveData<*>?

    suspend fun updateStats(date: OffsetDateTime, time: Long)

    fun resetDayStatsMonitor()
    fun resetWeekStatsMonitor()
    fun resetMonthStatsMonitor()
}
