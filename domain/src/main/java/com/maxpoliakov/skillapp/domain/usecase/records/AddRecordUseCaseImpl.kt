package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import com.maxpoliakov.skillapp.domain.repository.UserPreferenceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class AddRecordUseCaseImpl @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val skillRepository: SkillRepository,
    private val statsRepository: SkillStatsRepository,
    private val preferenceRepository: UserPreferenceRepository,
    private val clock: Clock,
) : AddRecordUseCase {

    override suspend fun run(record: Record): Long {
        if (skillRepository.getSkillById(record.skillId) == null) return -1

        return addRecordInternal(
            if (!dayHasStarted() && record.date == LocalDate.now(clock)) record.copy(date = record.date.minusDays(1))
            else record
        )
    }

    private suspend fun addRecordInternal(record: Record): Long {
        return coroutineScope {
            val recordIdAsync = async { recordsRepository.addRecord(record) }
            launch { skillRepository.increaseCount(record.skillId, record.count) }
            launch { statsRepository.addRecord(record) }
            recordIdAsync.await()
        }
    }

    private fun dayHasStarted() = LocalTime.now(clock) >= preferenceRepository.getDayStartTime()
}
