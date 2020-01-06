package com.jdevs.timeo.domain.activities

import com.jdevs.timeo.data.activities.ActivitiesRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {

    val activities get() = activitiesRepository.activities

    fun resetActivities() = activitiesRepository.resetActivitiesMonitor()
}
