package com.jdevs.timeo.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jdevs.timeo.data.stats.DAY_STATS_ENTRIES
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.monthSinceEpoch
import com.jdevs.timeo.shared.util.weeksSinceEpoch
import com.jdevs.timeo.ui.common.WeekDayFormatter
import com.jdevs.timeo.ui.common.YearMonthFormatter
import com.jdevs.timeo.ui.common.YearWeekFormatter
import com.jdevs.timeo.util.charts.toChartData
import org.threeten.bp.OffsetDateTime

open class StatsViewModel(getStats: GetStatsUseCase, activityId: String = "") : ViewModel() {
    val dayStats by lazy {
        getStats.getDayStats(activityId).map {
            it.toChartData(
                OffsetDateTime::minusDays,
                { daysSinceEpoch },
                WeekDayFormatter(),
                DAY_STATS_ENTRIES
            )
        }
    }

    val weekStats by lazy {
        getStats.getWeekStats(activityId).map {
            it.toChartData(OffsetDateTime::minusWeeks, { weeksSinceEpoch }, YearWeekFormatter())
        }
    }

    val monthStats by lazy {
        getStats.getMonthStats(activityId).map {
            it.toChartData(OffsetDateTime::minusMonths, { monthSinceEpoch }, YearMonthFormatter())
        }
    }
}
