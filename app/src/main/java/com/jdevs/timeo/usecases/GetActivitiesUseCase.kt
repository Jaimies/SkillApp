package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.source.ActivitiesRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {

    val activities get() = activitiesRepository.activities

    fun resetActivities() = activitiesRepository.resetActivitiesMonitor()
}
