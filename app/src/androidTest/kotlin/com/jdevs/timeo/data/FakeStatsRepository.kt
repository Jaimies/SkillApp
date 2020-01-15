package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.domain.repository.StatsRepository
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("EmptyFunctionBlock")
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
