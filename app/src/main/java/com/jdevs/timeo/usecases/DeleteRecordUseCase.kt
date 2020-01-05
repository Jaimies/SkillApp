package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.RecordsRepository
import com.jdevs.timeo.data.source.StatsRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {

    suspend fun deleteRecord(record: Record) {

        recordsRepository.deleteRecord(record)?.let { batch ->

            activitiesRepository.increaseTime(record.activityId, -record.time, batch)
            statsRepository.updateStats(record.creationDate, -record.time)
        }
    }
}
