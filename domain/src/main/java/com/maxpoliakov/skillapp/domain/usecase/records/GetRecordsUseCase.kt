package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(private val recordsRepository: RecordsRepository) {
    fun run() = recordsRepository.getRecords()
    fun run(skillId: Int) = recordsRepository.getRecordsBySkillId(skillId)
    fun run(skillIds: List<Int>) = recordsRepository.getRecordsBySkillIds(skillIds)
}
