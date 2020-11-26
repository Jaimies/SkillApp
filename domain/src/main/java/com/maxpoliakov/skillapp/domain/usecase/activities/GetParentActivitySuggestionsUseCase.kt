package com.maxpoliakov.skillapp.domain.usecase.activities

import com.maxpoliakov.skillapp.domain.model.Activity
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.ActivitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParentActivitySuggestionsUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {
    fun run(activityId: Id): Flow<List<Activity>> {
        return activitiesRepository.getParentActivitySuggestions(activityId)
    }
}
