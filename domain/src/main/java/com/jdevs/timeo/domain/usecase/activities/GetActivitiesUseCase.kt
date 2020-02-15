package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    val activities get() = activitiesRepository.activities
    fun getActivitiesRemote(fetchNewItems: Boolean) =
        activitiesRepository.getRemoteActivities(fetchNewItems)
}
