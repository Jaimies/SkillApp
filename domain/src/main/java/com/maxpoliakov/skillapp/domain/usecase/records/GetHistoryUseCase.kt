package com.maxpoliakov.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.shared.util.sumByLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: SkillStatsRepository,
) {
    fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>> {
        return getSkillIds(criteria).flatMapLatest(recordsRepository::getRecordsBySkillIds)
    }

    suspend fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>): Long {
        val ids = getSkillIds(criteria).first()
        return getCount(ids, range)
    }

    suspend fun getCount(criteria: SkillSelectionCriteria, date: LocalDate): Long {
        return getCount(criteria, date..date)
    }

    private fun getSkillIds(criteria: SkillSelectionCriteria): Flow<List<Id>> {
        return skillRepository
            .getSkills(criteria)
            .mapList { skill -> skill.id }
    }

    private suspend fun getCount(ids: List<Id>, range: ClosedRange<LocalDate>): Long {
        return ids.sumByLong { id -> statsRepository.getCount(id, range) }
    }
}
