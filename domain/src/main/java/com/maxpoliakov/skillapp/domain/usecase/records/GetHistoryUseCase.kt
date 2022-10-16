package com.maxpoliakov.skillapp.domain.usecase.records

import androidx.paging.PagingData
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.filterList
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import java.time.Duration
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

    suspend fun getTimeAtDate(criteria: SkillSelectionCriteria, date: LocalDate): Duration {
        val ids = getSkillIds(criteria) { skill ->
            skill.unit == MeasurementUnit.Millis
        }.first()

        return getTimeAtDate(ids, date)
    }

    private fun getSkillIds(
        criteria: SkillSelectionCriteria,
        additionalRequirements: (Skill) -> Boolean = { true }
    ): Flow<List<Id>> {
        return skillRepository
            .getSkills(criteria)
            .filterList(additionalRequirements)
            .mapList { skill -> skill.id }
    }

    private suspend fun getTimeAtDate(ids: List<Id>, date: LocalDate): Duration {
        return ids.sumByDuration { id -> getTimeAtDate(id, date) }
    }

    private suspend fun getTimeAtDate(id: Id, date: LocalDate): Duration {
        return statsRepository.getCountAtDate(id, date).let(Duration::ofMillis)
    }
}
