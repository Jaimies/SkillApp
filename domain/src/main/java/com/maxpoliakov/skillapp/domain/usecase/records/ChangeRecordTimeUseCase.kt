package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import javax.inject.Inject

class ChangeRecordTimeUseCase @Inject constructor(
    private val addRecord: AddRecordUseCase,
    private val deleteRecord: DeleteRecordUseCase,
    private val recordsRepository: RecordsRepository
) {
    suspend fun run(id: Int, time: Duration) = withContext(Dispatchers.IO) {
        val oldRecord = recordsRepository.getRecord(id)
        deleteRecord.run(id)
        addRecord.run(oldRecord.copy(time = time))
    }
}
