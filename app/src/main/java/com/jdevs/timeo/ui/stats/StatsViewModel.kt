package com.jdevs.timeo.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jdevs.timeo.data.stats.DAY_STATS_ENTRIES
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.ui.common.WeekDayFormatter
import com.jdevs.timeo.ui.common.YearMonthFormatter
import com.jdevs.timeo.ui.common.YearWeekFormatter
import com.jdevs.timeo.util.charts.ChartState
import com.jdevs.timeo.util.charts.toEntries
import org.threeten.bp.temporal.ChronoUnit.DAYS
import org.threeten.bp.temporal.ChronoUnit.MONTHS
import org.threeten.bp.temporal.ChronoUnit.WEEKS

open class StatsViewModel(
    getStats: GetStatsUseCase,
    activityId: String
) : ViewModel() {

    val dayStats by lazy {
        getStats.getDayStats(activityId).map { stats ->
            ChartState(
                stats.toEntries(DAYS, DAY_STATS_ENTRIES),
                WeekDayFormatter()
            )
        }
    }

    val weekStats by lazy {
        getStats.getWeekStats(activityId).map { stats ->
            ChartState(stats.toEntries(WEEKS), YearWeekFormatter())
        }
    }

    val monthStats by lazy {
        getStats.getMonthStats(activityId).map { stats ->
            ChartState(stats.toEntries(MONTHS), YearMonthFormatter())
        }
    }
}
