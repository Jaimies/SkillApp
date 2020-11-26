package com.maxpoliakov.skillapp.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import com.maxpoliakov.skillapp.domain.usecase.stats.GetStatsUseCase
import com.maxpoliakov.skillapp.ui.stats.StatsViewModel

class ProfileViewModel @ViewModelInject constructor(
    getStatsUseCase: GetStatsUseCase
) : StatsViewModel(getStatsUseCase, -1)
