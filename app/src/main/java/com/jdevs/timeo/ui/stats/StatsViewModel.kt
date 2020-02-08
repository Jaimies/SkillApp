package com.jdevs.timeo.ui.stats

import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.model.StatsItem
import com.jdevs.timeo.model.StatsTypes.DAY
import com.jdevs.timeo.model.StatsTypes.MONTH
import com.jdevs.timeo.model.StatsTypes.WEEK
import com.jdevs.timeo.model.mapToPresentation
import com.jdevs.timeo.ui.common.viewmodel.ListViewModel
import com.jdevs.timeo.util.mapTo
import com.jdevs.timeo.util.toPagedList
import javax.inject.Inject
import javax.inject.Singleton

private const val STATS_PAGE_SIZE = 40

@Singleton
class StatsViewModel @Inject constructor(private val stats: GetStatsUseCase) :
    ListViewModel<StatsItem>() {

    override val localLiveData
        get() = when (statsType) {

            DAY -> stats.dayStats.toPagedList(STATS_PAGE_SIZE, DayStats::mapToPresentation)
            WEEK -> stats.weekStats.toPagedList(STATS_PAGE_SIZE, WeekStats::mapToPresentation)
            MONTH -> stats.monthStats.toPagedList(STATS_PAGE_SIZE, MonthStats::mapToPresentation)
            else -> throw IllegalArgumentException("Unknown stats type $statsType")
        }

    override val remoteLiveDatas
        get() = when (statsType) {
            DAY -> stats.dayStatsRemote.mapTo(DayStats::mapToPresentation)
            WEEK -> stats.weekStatsRemote.mapTo(WeekStats::mapToPresentation)
            MONTH -> stats.monthStatsRemote.mapTo(MonthStats::mapToPresentation)
            else -> throw IllegalArgumentException("Unknown stats type $statsType")
        }

    fun setStatsType(type: Int) {

        statsType = type
    }

    private var statsType = DAY
}
