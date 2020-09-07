package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.model.Id
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivityByIdUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {
    fun run(id: Id) = activitiesRepository.getActivityById(id)
}
