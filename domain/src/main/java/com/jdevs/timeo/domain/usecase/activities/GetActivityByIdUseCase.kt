package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivityByIdUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    operator fun invoke(id: String) = activitiesRepository.getActivityById(id)
}
