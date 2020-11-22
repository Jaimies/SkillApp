package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import com.jdevs.timeo.domain.repository.StatsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val activitiesRepository: ActivitiesRepository,
    private val statsRepository: StatsRepository
) {
    suspend fun run(record: Record) {
        withContext(IO) {
            launch { recordsRepository.addRecord(record) }
            launch { activitiesRepository.increaseTime(record.activityId, record.time) }
            launch { statsRepository.recordStats(record) }
        }
    }

    private suspend fun StatsRepository.recordStats(record: Record) {
        withContext(IO) {
            launch { addDayRecord(record) }
            launch { addWeekRecord(record) }
            launch { addMonthRecord(record) }
        }
    }
}
