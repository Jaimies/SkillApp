package com.jdevs.timeo.data.source.remote

import org.threeten.bp.OffsetDateTime

interface StatsRemoteDataSource {

    val dayStats: ItemsLiveData?
    val weekStats: ItemsLiveData?
    val monthStats: ItemsLiveData?

    suspend fun updateStats(date: OffsetDateTime, time: Long)

    fun resetDayStatsMonitor()
    fun resetWeekStatsMonitor()
    fun resetMonthStatsMonitor()
}
