package com.jdevs.timeo.domain.activities

import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.model.Activity
import javax.inject.Inject

class DeleteActivityUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    suspend fun deleteActivity(activity: Activity) = activitiesRepository.deleteActivity(activity)
}
