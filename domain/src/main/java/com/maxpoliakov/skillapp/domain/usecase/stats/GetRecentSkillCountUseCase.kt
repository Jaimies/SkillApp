package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.domain.time.DateProvider
import javax.inject.Inject

class GetRecentSkillCountUseCase @Inject constructor(
    statsRepository: SkillStatsRepository,
    dateProvider: DateProvider,
) : GetRecentCountUseCaseImpl(statsRepository, dateProvider)
