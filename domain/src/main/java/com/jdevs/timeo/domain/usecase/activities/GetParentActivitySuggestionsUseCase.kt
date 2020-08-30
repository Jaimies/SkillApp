package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetParentActivitySuggestionsUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {
    fun run(activityId: Int) =
        activitiesRepository.getParentActivitySuggestions(activityId)
}
