package com.maxpoliakov.skillapp.domain.usecase.stats

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.shared.util.sumByDuration
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject

class GetTimeAtDateUseCase @Inject constructor(
    private val skillRepository: SkillRepository,
    private val statsRepository: SkillStatsRepository,
) {
    suspend fun run(date: LocalDate): Duration {
        val ids = skillRepository
            .getSkillsWithMeasurementUnit(MeasurementUnit.Millis)
            .map(Skill::id)

        return run(ids, date)
    }

    suspend fun run(ids: List<Id>, date: LocalDate): Duration {
        return ids.sumByDuration { id -> run(id, date) }
    }

    suspend fun run(id: Id, date: LocalDate): Duration {
        return statsRepository.getCountAtDate(id, date).let(Duration::ofMillis)
    }
}
