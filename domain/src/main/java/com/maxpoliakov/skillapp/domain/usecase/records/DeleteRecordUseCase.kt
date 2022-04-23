package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository
) {
    suspend fun run(id: Int) = withContext(IO) {
        val record = recordsRepository.getRecord(id) ?: return@withContext

        skillRepository.decreaseCount(record.skillId, record.count)
        recordsRepository.deleteRecord(record)
        statsRepository.addRecord(record.copy(count = -record.count))
    }
}
