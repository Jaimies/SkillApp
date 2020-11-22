package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import com.jdevs.timeo.domain.repository.RecordsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val activitiesRepository: ActivitiesRepository
) {
    suspend fun run(record: Record) {
        withContext(IO) {
            activitiesRepository.decreaseTime(record.activityId, record.time)
            recordsRepository.deleteRecord(record)
        }
    }
}
