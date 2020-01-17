package com.jdevs.timeo.ui.stats

import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.StatsTypes.DAY
import com.jdevs.timeo.util.StatsTypes.MONTH
import com.jdevs.timeo.util.StatsTypes.WEEK
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsViewModel @Inject constructor(private val stats: GetStatsUseCase) : ListViewModel() {

    override val liveData
        get() = when (statsType) {

            DAY -> stats.dayStats
            WEEK -> stats.weekStats
            MONTH -> stats.monthStats
            else -> null
        }

    fun setStatsType(type: Int) {

        statsType = type

        when (type) {

            DAY -> stats.resetDayStats()
            WEEK -> stats.resetWeekStats()
            MONTH -> stats.resetMonthStats()
        }
    }

    private var statsType = DAY
}
