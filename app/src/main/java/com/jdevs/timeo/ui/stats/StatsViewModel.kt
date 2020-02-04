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

    override val localLiveData
        get() = when (statsType) {

            DAY -> stats.dayStats
            WEEK -> stats.weekStats
            MONTH -> stats.monthStats
            else -> throw IllegalArgumentException("Unknown stats type $statsType")
        }

    override val remoteLiveDatas
        get() = when (statsType) {
            DAY -> stats.dayStatsRemote
            WEEK -> stats.weekStatsRemote
            MONTH -> stats.monthStatsRemote
            else -> throw IllegalArgumentException("Unknown stats type $statsType")
        }

    fun setStatsType(type: Int) {

        statsType = type
    }

    private var statsType = DAY
}
