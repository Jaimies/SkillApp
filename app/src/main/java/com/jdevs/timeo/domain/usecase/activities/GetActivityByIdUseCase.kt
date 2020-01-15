package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivityByIdUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    fun getActivityById(id: Int, documentId: String) =
        activitiesRepository.getActivityById(id, documentId)
}
