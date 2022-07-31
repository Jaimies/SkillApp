package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import javax.inject.Inject

class GetRecentSkillCountUseCase @Inject constructor(
    statsRepository: SkillStatsRepository,
) : GetRecentCountUseCaseImpl(statsRepository)
