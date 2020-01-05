package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.RecordsRepository
import com.jdevs.timeo.data.source.StatsRepository
import javax.inject.Inject

class AddRecordUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {

    suspend fun addRecord(record: Record) {

        recordsRepository.addRecord(record)?.also { batch ->

            activitiesRepository.increaseTime(record.activityId, record.time, batch)
            statsRepository.updateStats(record.creationDate, record.time)
        }
    }
}
