package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import com.maxpoliakov.skillapp.domain.time.DateProvider
import javax.inject.Inject

class GetRecentGroupCountUseCase @Inject constructor(
    statsRepository: GroupStatsRepository,
    dateProvider: DateProvider,
) : GetRecentCountUseCaseImpl(statsRepository, dateProvider)
