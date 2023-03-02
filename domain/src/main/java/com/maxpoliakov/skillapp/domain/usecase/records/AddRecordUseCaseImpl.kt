package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddRecordUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: SkillStatsRepository,
): AddRecordUseCase {
    override suspend fun run(record: Record): Long {
        if (skillRepository.getSkillById(record.skillId) == null) return -1

        return coroutineScope {
            val recordIdAsync = async { recordsRepository.addRecord(record) }
            launch { skillRepository.increaseCount(record.skillId, record.count) }
            launch { statsRepository.addRecord(record) }
            recordIdAsync.await()
        }
    }
}
