package com.jdevs.timeo.ui.stats

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.domain.stats.GetStatsUseCase
import com.jdevs.timeo.util.StatsTypes.DAY
import com.jdevs.timeo.util.StatsTypes.MONTH
import com.jdevs.timeo.util.StatsTypes.WEEK
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsViewModel @Inject constructor(
    private val getStatsUseCase: GetStatsUseCase
) : ListViewModel() {

    override val liveData
        get() = when (statsType) {

            DAY -> getStatsUseCase.dayStats
            WEEK -> getStatsUseCase.weekStats
            MONTH -> getStatsUseCase.monthStats
            else -> null
        }

    fun setStatsType(type: Int) {

        statsType = type

        when (type) {

            DAY -> getStatsUseCase.resetDayStats()
            WEEK -> getStatsUseCase.resetWeekStats()
            MONTH -> getStatsUseCase.resetMonthStats()
        }
    }

    private var statsType = DAY
}
