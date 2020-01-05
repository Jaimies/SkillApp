package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesRepository
import javax.inject.Inject

class DeleteActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {

    suspend fun deleteActivity(activity: Activity) = activitiesRepository.deleteActivity(activity)
}
