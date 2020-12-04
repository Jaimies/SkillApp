package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import java.time.LocalDate
import javax.inject.Inject

class ChangeRecordDateUseCase @Inject constructor(
    private val addRecord: AddRecordUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val recordsRepository: RecordsRepository
) {

    suspend fun run(id: Int, newDate: LocalDate) {
        val oldRecord = recordsRepository.getRecord(id)
        deleteRecord.run(id)
        addRecord.run(oldRecord.copy(date = newDate))
    }
}
