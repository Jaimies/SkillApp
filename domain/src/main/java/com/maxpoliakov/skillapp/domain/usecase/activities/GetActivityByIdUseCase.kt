package com.maxpoliakov.skillapp.domain.usecase.activities

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetActivityByIdUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {
    fun run(id: Id) = activitiesRepository.getActivityById(id)
}
