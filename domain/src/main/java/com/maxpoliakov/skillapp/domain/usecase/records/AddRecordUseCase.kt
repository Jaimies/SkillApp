package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRecordUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository
) {
    suspend fun run(record: Record): Long {
        if (skillRepository.getSkillById(record.skillId) == null) return -1

        return withContext(IO) {
            val recordIdAsync = async { recordsRepository.addRecord(record) }
            launch { skillRepository.increaseTime(record.skillId, record.time) }
            launch { statsRepository.addRecord(record) }
            recordIdAsync.await()
        }
    }
}
