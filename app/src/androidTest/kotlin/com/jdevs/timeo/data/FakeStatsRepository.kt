package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.data.stats.StatsRepository
import com.jdevs.timeo.model.DayStats
import com.jdevs.timeo.model.MonthStats
import com.jdevs.timeo.model.WeekStats
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeStatsRepository @Inject constructor() : StatsRepository {

    private val dayStatsList = mutableListOf<DayStats>()
    private val weekStatsList = mutableListOf<WeekStats>()
    private val monthStatsList = mutableListOf<MonthStats>()

    override val dayStats = MutableLiveData(dayStatsList.asPagedList())
    override val weekStats = MutableLiveData(weekStatsList.asPagedList())
    override val monthStats = MutableLiveData(monthStatsList.asPagedList())

    override suspend fun updateStats(date: OffsetDateTime, time: Long) {}

    override fun resetDayStatsMonitor() {}
    override fun resetWeekStatsMonitor() {}
    override fun resetMonthStatsMonitor() {}
}
