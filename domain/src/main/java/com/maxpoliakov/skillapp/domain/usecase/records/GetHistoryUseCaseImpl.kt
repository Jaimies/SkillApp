package com.maxpoliakov.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject

class GetHistoryUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: StatsRepository,
) : GetHistoryUseCase {
    override fun getRecords(criteria: SkillSelectionCriteria): Flow<PagingData<Record>> {
        return getSkillIds(criteria).flatMapLatest(recordsRepository::getRecordsBySkillIds)
    }

    override fun getCount(criteria: SkillSelectionCriteria, range: ClosedRange<LocalDate>): Flow<Long> {
        return getSkillIds(criteria).flatMapLatest { ids ->
            getCount(ids, range)
        }
    }

    override fun getCount(criteria: SkillSelectionCriteria, date: LocalDate): Flow<Long> {
        return getCount(criteria, date..date)
    }

    private fun getSkillIds(criteria: SkillSelectionCriteria): Flow<List<Id>> {
        return skillRepository
            .getSkills(criteria)
            .mapList { skill -> skill.id }
    }

    private fun getCount(ids: List<Id>, range: ClosedRange<LocalDate>): Flow<Long> {
        return combine(
            ids.map { statsRepository.getCount(it, range) },
            Array<Long>::sum,
        )
    }
}
