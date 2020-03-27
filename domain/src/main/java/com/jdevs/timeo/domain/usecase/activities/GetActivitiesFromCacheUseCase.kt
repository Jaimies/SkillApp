package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivitiesFromCacheUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    operator fun invoke(activityIdToExclude: String) =
        activitiesRepository.getActivitiesFromCache(activityIdToExclude)
}
