package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository
) {

    suspend operator fun invoke(activity: Activity) {

        activitiesRepository.saveActivity(activity)?.let { batch ->

            recordsRepository.renameRecords(activity.documentId, activity.name, batch)
        }
    }
}
