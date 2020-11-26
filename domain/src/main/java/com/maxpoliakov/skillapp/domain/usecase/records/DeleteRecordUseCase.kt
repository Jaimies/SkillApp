package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.ActivitiesRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
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
