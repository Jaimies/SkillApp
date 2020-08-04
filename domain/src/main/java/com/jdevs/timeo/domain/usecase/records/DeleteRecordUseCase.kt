package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(private val recordsRepository: RecordsRepository) {
    suspend fun run(record: Record) = recordsRepository.deleteRecord(record)
}
