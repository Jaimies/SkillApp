package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.RecordsRepository
import javax.inject.Inject

class ActivityDetailsUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository
) {

    fun getActivityById(id: Int, documentId: String) =
        activitiesRepository.getActivityById(id, documentId)

    suspend fun addRecord(record: Record) {

        val batch = recordsRepository.addRecord(record)
        activitiesRepository.increaseTime(record.activityId, record.time, batch)
    }
}
