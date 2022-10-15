package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SelectionCriteria
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.filterList
import com.maxpoliakov.skillapp.shared.util.mapList
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: SkillStatsRepository,
) {
    fun getRecords(criteria: SelectionCriteria) = recordsRepository.getRecords(criteria)

    suspend fun getTimeAtDate(criteria: SelectionCriteria, date: LocalDate): Duration {
        val ids = skillRepository
            .getSkills(criteria)
            .filterList { skill -> skill.unit == MeasurementUnit.Millis }
            .mapList { skill -> skill.id }
            .first()

        return getTimeAtDate(ids, date)
    }

    private suspend fun getTimeAtDate(ids: List<Id>, date: LocalDate): Duration {
        return ids.sumByDuration { id -> getTimeAtDate(id, date) }
    }

    private suspend fun getTimeAtDate(id: Id, date: LocalDate): Duration {
        return statsRepository.getCountAtDate(id, date).let(Duration::ofMillis)
    }
}
