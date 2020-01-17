package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    operator fun invoke() = activitiesRepository.activities
    fun resetActivities() = activitiesRepository.resetMonitor()
}
