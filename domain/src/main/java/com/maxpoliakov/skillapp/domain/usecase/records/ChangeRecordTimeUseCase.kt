package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import javax.inject.Inject

class ChangeRecordTimeUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {
    suspend fun run(id: Int, time: Duration) = withContext(Dispatchers.IO) {
        val oldRecord = recordsRepository.getRecord(id)
        val newRecord = oldRecord.copy(time = time)
        recordsRepository.updateRecord(newRecord)
        statsRepository.addRecord(newRecord.copy(time = newRecord.time - oldRecord.time))
    }
}
