package com.maxpoliakov.skillapp.domain.usecase.activities

import com.maxpoliakov.skillapp.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {
    fun run() = activitiesRepository.getActivities()
}
