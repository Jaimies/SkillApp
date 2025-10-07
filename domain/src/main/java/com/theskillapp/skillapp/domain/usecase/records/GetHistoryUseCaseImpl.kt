package com.theskillapp.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.repository.RecordsRepository
import com.theskillapp.skillapp.domain.repository.SkillRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetHistoryUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
) : GetHistoryUseCase {
    override fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>> {
        return skillRepository.getSkills(criteria).flatMapLatest(this::getRecords)
    }

    private fun getRecords(skills: List<Skill>): Flow<PagingData<Record>> {
        return recordsRepository.getRecordsBySkillIds(skills.map { skill -> skill.id })
    }
}
