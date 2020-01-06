package com.jdevs.timeo.domain.usecase.activities

import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.data.records.RecordsRepository
import com.jdevs.timeo.domain.model.Activity
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository
) {

    suspend fun saveActivity(activity: Activity) {

        activitiesRepository.saveActivity(activity)?.let { batch ->

            recordsRepository.renameRecords(activity.documentId, activity.name, batch)
        }
    }
}
