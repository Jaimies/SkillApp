package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class AddActivityUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    suspend fun addActivity(activity: Activity) = activitiesRepository.addActivity(activity)
}
