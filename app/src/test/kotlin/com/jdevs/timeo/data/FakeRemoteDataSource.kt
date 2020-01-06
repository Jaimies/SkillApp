package com.jdevs.timeo.data

import com.jdevs.timeo.data.records.RecordsRemoteDataSource
import com.jdevs.timeo.model.Activity
import com.jdevs.timeo.model.DayStats
import com.jdevs.timeo.model.Record

class FakeRemoteDataSource(
    activityList: List<Activity> = emptyList(),
    recordList: List<Record> = emptyList(),
    statsList: List<DayStats> = emptyList()
) : FakeDataSource(activityList, recordList, statsList),
    RecordsRemoteDataSource {

    override fun resetActivitiesMonitor() {}

    override fun resetRecordsMonitor() {}

    override fun resetDayStatsMonitor() {}

    override fun resetWeekStatsMonitor() {}

    override fun resetMonthStatsMonitor() {}
}