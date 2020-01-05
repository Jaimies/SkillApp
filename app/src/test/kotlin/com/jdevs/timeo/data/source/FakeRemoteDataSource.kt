package com.jdevs.timeo.data.source

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.remote.RecordsRemoteDataSource

class FakeRemoteDataSource(
    activityList: List<Activity> = emptyList(),
    recordList: List<Record> = emptyList(),
    statsList: List<DayStats> = emptyList()
) : FakeDataSource(activityList, recordList, statsList), RecordsRemoteDataSource {

    override fun resetActivitiesMonitor() {}

    override fun resetRecordsMonitor() {}

    override fun resetDayStatsMonitor() {}

    override fun resetWeekStatsMonitor() {}

    override fun resetMonthStatsMonitor() {}
}