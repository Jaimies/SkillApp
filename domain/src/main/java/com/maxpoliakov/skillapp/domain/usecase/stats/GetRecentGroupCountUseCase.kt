package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import java.time.Clock
import javax.inject.Inject

class GetRecentGroupCountUseCase @Inject constructor(
    statsRepository: GroupStatsRepository,
    clock: Clock,
) : GetRecentCountUseCaseImpl(statsRepository, clock)
