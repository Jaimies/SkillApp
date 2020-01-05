package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.RecordsRepository
import com.jdevs.timeo.data.source.StatsRepository
import javax.inject.Inject

class EditActivityUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {

    suspend fun addActivity(activity: Activity) = activitiesRepository.addActivity(activity)

    suspend fun saveActivity(activity: Activity) {

        val batch = activitiesRepository.saveActivity(activity)
        recordsRepository.renameRecords(activity.documentId, activity.name, batch)
    }

    suspend fun addRecord(record: Record) {

        val batch = recordsRepository.addRecord(record)

        activitiesRepository.increaseTime(record.activityId, record.time, batch)
        statsRepository.updateStats(record.creationDate, record.time)
    }

    suspend fun deleteActivity(activity: Activity) = activitiesRepository.deleteActivity(activity)
}
