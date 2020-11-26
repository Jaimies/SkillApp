package com.maxpoliakov.skillapp.domain.usecase.activities

import com.maxpoliakov.skillapp.domain.model.Activity
import com.maxpoliakov.skillapp.domain.repository.ActivitiesRepository
import javax.inject.Inject

class AddActivityUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {
    suspend fun run(activity: Activity) {
        activitiesRepository.addActivity(activity)
    }
}
