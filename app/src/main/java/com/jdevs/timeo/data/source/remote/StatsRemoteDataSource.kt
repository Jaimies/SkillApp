package com.jdevs.timeo.data.source.remote

import com.jdevs.timeo.data.source.StatsDataSource

interface StatsRemoteDataSource : StatsDataSource {

    override val dayStats: ItemsLiveData?
    override val weekStats: ItemsLiveData?
    override val monthStats: ItemsLiveData?

    fun resetDayStatsMonitor()
    fun resetWeekStatsMonitor()
    fun resetMonthStatsMonitor()
}
