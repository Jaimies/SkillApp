package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import javax.inject.Inject

class AddRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val activitiesRepository: ActivitiesRepository,
    private val statsRepository: StatsRepository
) {
    suspend fun run(record: Record) {
        recordsRepository.addRecord(record)
        activitiesRepository.increaseTime(record.activity.id, record.time)
        statsRepository.registerStats(record)
    }

    private fun StatsRepository.registerStats(record: Record) {
        registerDayStats(record.activity.id, record.time)
        registerWeekStats(record.activity.id, record.time)
        registerMonthStats(record.activity.id, record.time)
        registerTotalStats(record.time)
    }
}
