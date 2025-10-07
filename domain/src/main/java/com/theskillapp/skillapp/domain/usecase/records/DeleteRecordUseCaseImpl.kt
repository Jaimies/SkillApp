package com.theskillapp.skillapp.domain.usecase.records

import com.theskillapp.skillapp.domain.repository.RecordsRepository
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.repository.StatsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteRecordUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository,
): DeleteRecordUseCase {
    override suspend fun run(id: Int) {
        val record = recordsRepository.getRecord(id).first() ?: return

        skillRepository.decreaseCount(record.skillId, record.count)
        recordsRepository.deleteRecord(record)
        statsRepository.addRecord(record.copy(count = -record.count))
    }
}
