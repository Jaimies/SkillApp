package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.data.activities.ActivitiesRepository
import com.jdevs.timeo.data.records.RecordsRepository
import com.jdevs.timeo.data.stats.StatsRepository
import com.jdevs.timeo.domain.model.Record
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
