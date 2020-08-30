package com.jdevs.timeo.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.ui.stats.StatsViewModel

class ProfileViewModel @ViewModelInject constructor(
    getStatsUseCase: GetStatsUseCase
) : StatsViewModel(getStatsUseCase, -1)
