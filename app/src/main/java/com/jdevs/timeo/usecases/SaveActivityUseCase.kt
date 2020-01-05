package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.RecordsRepository
import javax.inject.Inject

class SaveActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository
) {

    suspend fun saveActivity(activity: Activity) {

        activitiesRepository.saveActivity(activity)?.also { batch ->

            recordsRepository.renameRecords(activity.documentId, activity.name, batch)
        }
    }
}
