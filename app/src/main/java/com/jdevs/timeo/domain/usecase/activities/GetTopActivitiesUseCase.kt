package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.repository.ActivitiesRepository
import javax.inject.Inject

class GetTopActivitiesUseCase @Inject constructor(private val activitiesRepository: ActivitiesRepository) {

    val topActivities get() = activitiesRepository.topActivities
}
