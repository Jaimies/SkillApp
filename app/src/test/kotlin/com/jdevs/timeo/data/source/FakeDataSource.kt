package com.jdevs.timeo.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.Record

class FakeDataSource(
    activityList: List<Activity> = emptyList(),
    recordList: List<Record> = emptyList(),
    statsList: List<DayStats> = emptyList()
) : TimeoDataSource {

    override val activities: LiveData<MutableList<Activity>> get() = _activities
    override val records: LiveData<MutableList<Record>> get() = _records
    override val dayStats: LiveData<MutableList<DayStats>> get() = _dayStats
    override val weekStats: LiveData<MutableList<DayStats>> get() = _dayStats
    override val monthStats: LiveData<MutableList<DayStats>> get() = _dayStats

    private val _activities = MutableLiveData(activityList.toMutableList())
    private val _records = MutableLiveData(recordList.toMutableList())
    private val _dayStats = MutableLiveData(statsList.toMutableList())

    override fun getActivityById(id: Int, documentId: String) = MutableLiveData(Activity())
    override suspend fun addActivity(activity: Activity) {}
    override suspend fun addRecord(record: Record) {}
    override suspend fun deleteActivity(activity: Activity) {}
    override suspend fun deleteRecord(record: Record) {}
    override suspend fun saveActivity(activity: Activity) {}
    override fun resetRecordsMonitor() {}
    override fun resetActivitiesMonitor() {}
    override fun resetDayStatsMonitor() {}
    override fun resetWeekStatsMonitor() {}
    override fun resetMonthStatsMonitor() {}
}
