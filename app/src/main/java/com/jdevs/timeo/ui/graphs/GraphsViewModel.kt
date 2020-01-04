package com.jdevs.timeo.ui.graphs

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.util.GraphTypes.DAY
import com.jdevs.timeo.util.GraphTypes.MONTH
import com.jdevs.timeo.util.GraphTypes.WEEK
import javax.inject.Inject

class GraphsViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ListViewModel() {

    override val liveData
        get() = when (graphType) {

            DAY -> repository.dayStats
            WEEK -> repository.weekStats
            MONTH -> repository.monthStats
            else -> null
        }

    fun setGraphType(type: Int) {

        graphType = type

        when (type) {

            DAY -> repository.resetDayStatsMonitor()
            WEEK -> repository.resetWeekStatsMonitor()
            MONTH -> repository.resetMonthStatsMonitor()
        }
    }

    private var graphType = DAY
}
