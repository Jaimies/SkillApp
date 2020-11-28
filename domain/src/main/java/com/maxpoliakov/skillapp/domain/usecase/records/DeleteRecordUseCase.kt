package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository
) {
    suspend fun run(record: Record) {
        withContext(IO) {
            skillRepository.decreaseTime(record.skillId, record.time)
            recordsRepository.deleteRecord(record)
        }
    }
}
