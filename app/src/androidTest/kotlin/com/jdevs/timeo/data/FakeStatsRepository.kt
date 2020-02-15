package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeStatsRepository @Inject constructor() : StatsRepository {

    private val dayStatsList = mutableListOf<DayStats>()
    private val weekStatsList = mutableListOf<WeekStats>()
    private val monthStatsList = mutableListOf<MonthStats>()

    override val dayStats = MutableLiveData(dayStatsList.toList())
    override val weekStats = MutableLiveData(weekStatsList.toList())
    override val monthStats = MutableLiveData(monthStatsList.toList())
}
