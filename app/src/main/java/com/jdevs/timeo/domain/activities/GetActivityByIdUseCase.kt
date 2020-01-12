package com.jdevs.timeo.domain.activities

import com.jdevs.timeo.data.activities.ActivitiesRepository
import javax.inject.Inject

class GetActivityByIdUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    fun getActivityById(id: Int, documentId: String) =
        activitiesRepository.getActivityById(id, documentId)
}
