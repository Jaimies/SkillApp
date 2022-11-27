package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class EditRecordUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val deleteRecord: DeleteRecordUseCase,
    private val addRecord: AddRecordUseCase,
) : EditRecordUseCase {
    override suspend fun changeDate(recordId: Id, newDate: LocalDate) {
        editRecord(recordId) { record -> record.copy(date = newDate) }
    }

    override suspend fun changeCount(recordId: Id, newCount: Long) {
        editRecord(recordId) { record -> record.copy(count = newCount) }
    }

    private suspend fun editRecord(recordId: Id, change: (Record) -> Record) = withContext(Dispatchers.IO) {
        val oldRecord = recordsRepository.getRecord(recordId) ?: return@withContext

        deleteRecord.run(recordId)
        addRecord.run(change.invoke(oldRecord))
    }
}
