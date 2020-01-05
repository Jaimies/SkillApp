package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.ActivitiesRepository
import com.jdevs.timeo.data.source.RecordsRepository
import com.jdevs.timeo.data.source.StatsRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {

    val records get() = recordsRepository.records

    fun resetRecords() = recordsRepository.resetRecordsMonitor()

    suspend fun deleteRecord(record: Record) {

        val batch = recordsRepository.deleteRecord(record)
        activitiesRepository.increaseTime(record.activityId, -record.time, batch)
        statsRepository.updateStats(record.creationDate, -record.time)
    }
}
