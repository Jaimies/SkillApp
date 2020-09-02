package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {
    suspend fun run(activity: Activity) {
        activitiesRepository.saveActivity(activity)
    }
}
