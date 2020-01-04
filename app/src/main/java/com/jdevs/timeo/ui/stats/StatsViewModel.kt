package com.jdevs.timeo.ui.stats

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.util.StatsTypes.DAY
import com.jdevs.timeo.util.StatsTypes.MONTH
import com.jdevs.timeo.util.StatsTypes.WEEK
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ListViewModel() {

    override val liveData
        get() = when (statsType) {

            DAY -> repository.dayStats
            WEEK -> repository.weekStats
            MONTH -> repository.monthStats
            else -> null
        }

    fun setStatsType(type: Int) {

        statsType = type

        when (type) {

            DAY -> repository.resetDayStatsMonitor()
            WEEK -> repository.resetWeekStatsMonitor()
            MONTH -> repository.resetMonthStatsMonitor()
        }
    }

    private var statsType = DAY
}
