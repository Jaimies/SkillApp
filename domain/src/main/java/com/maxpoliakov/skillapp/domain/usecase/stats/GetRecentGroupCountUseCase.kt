package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import javax.inject.Inject

class GetRecentGroupCountUseCase @Inject constructor(
    statsRepository: GroupStatsRepository,
) : GetRecentCountUseCaseImpl(statsRepository)
