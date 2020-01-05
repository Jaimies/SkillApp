package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.source.ActivitiesRepository
import javax.inject.Inject

class GetActivityByIdUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {

    fun getActivityById(id: Int, documentId: String) =
        activitiesRepository.getActivityById(id, documentId)
}
