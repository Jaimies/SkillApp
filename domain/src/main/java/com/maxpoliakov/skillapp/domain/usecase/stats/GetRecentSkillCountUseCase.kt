package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import java.time.Clock
import javax.inject.Inject

class GetRecentSkillCountUseCase @Inject constructor(
    statsRepository: SkillStatsRepository,
    clock: Clock,
) : GetRecentCountUseCaseImpl(statsRepository, clock)
