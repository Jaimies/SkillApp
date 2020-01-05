package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesRepository
import javax.inject.Inject

class AddActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {

    suspend fun addActivity(activity: Activity) = activitiesRepository.addActivity(activity)
}
