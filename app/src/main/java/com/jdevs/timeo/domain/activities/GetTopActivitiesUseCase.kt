package com.jdevs.timeo.domain.activities

import com.jdevs.timeo.data.activities.ActivitiesRepository
import javax.inject.Inject

class GetTopActivitiesUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    fun getTopActivities() = activitiesRepository.getTopActivities()
}
