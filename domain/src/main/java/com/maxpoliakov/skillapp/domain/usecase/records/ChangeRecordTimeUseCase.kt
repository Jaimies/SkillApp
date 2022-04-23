package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import javax.inject.Inject

class ChangeRecordTimeUseCase @Inject constructor(
    private val addRecord: AddRecordUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val recordsRepository: RecordsRepository
) {
    suspend fun run(id: Int, count: Long) = withContext(Dispatchers.IO) {
        val oldRecord = recordsRepository.getRecord(id) ?: return@withContext

        deleteRecord.run(id)
        addRecord.run(oldRecord.copy(count = count))
    }
}
