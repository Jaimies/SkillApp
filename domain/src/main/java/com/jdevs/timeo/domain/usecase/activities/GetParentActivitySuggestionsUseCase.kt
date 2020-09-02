package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParentActivitySuggestionsUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {
    fun run(activityId: Int): Flow<List<Activity>> {
        return activitiesRepository.getParentActivitySuggestions(activityId)
    }
}
