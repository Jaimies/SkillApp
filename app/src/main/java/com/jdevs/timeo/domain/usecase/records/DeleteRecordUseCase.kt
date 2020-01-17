package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {

    suspend operator fun invoke(record: Record) {

        recordsRepository.deleteRecord(record)?.let { batch ->

            activitiesRepository.increaseTime(record.activityId, -record.time, batch)
            statsRepository.updateStats(record.creationDate, -record.time)
        }
    }
}
