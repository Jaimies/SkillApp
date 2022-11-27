package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ChangeRecordDateUseCaseImpl @Inject constructor(
    private val addRecord: AddRecordUseCaseImpl,
    private val deleteRecord: DeleteRecordUseCaseImpl,
    private val recordsRepository: RecordsRepository
): ChangeRecordDateUseCase {

    override suspend fun run(id: Int, newDate: LocalDate) = withContext(Dispatchers.IO){
        val oldRecord = recordsRepository.getRecord(id) ?: return@withContext

        deleteRecord.run(id)
        addRecord.run(oldRecord.copy(date = newDate))
    }
}
