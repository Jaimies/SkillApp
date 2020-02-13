package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.WeekStats

interface StatsRepository {

    val dayStats: LiveData<List<DayStats>>
    val weekStats: LiveData<List<WeekStats>>
    val monthStats: LiveData<List<MonthStats>>
}
