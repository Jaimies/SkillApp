package com.jdevs.timeo.domain.repository

import androidx.lifecycle.LiveData
import com.jdevs.timeo.domain.model.Statistic

interface StatsRepository {
    val dayStats: LiveData<List<Statistic>>
    val weekStats: LiveData<List<Statistic>>
    val monthStats: LiveData<List<Statistic>>
}
