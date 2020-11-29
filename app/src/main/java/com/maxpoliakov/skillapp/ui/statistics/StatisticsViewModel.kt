package com.maxpoliakov.skillapp.ui.statistics

import androidx.hilt.lifecycle.ViewModelInject
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel

class StatisticsViewModel @ViewModelInject constructor(
    getStatsUseCase: GetStatsUseCase
) : StatsViewModel(getStatsUseCase, -1)
