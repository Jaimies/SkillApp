package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.domain.model.Activity
import javax.inject.Inject

class AddActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {

    suspend fun addActivity(activity: Activity) = activitiesRepository.addActivity(activity)
}
