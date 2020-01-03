package com.jdevs.timeo.data.source.remote

import com.jdevs.timeo.data.source.TimeoDataSource

interface TimeoRemoteDataSource : TimeoDataSource {

    fun resetActivitiesMonitor()
    fun resetRecordsMonitor()
    fun resetDayStatsMonitor()
    fun resetWeekStatsMonitor()
    fun resetMonthStatsMonitor()
}
