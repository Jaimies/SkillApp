package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import java.time.LocalDate
import javax.inject.Inject

class ChangeRecordDateUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository,
    private val statsRepository: StatsRepository
) {

    suspend fun run(id: Int, newDate: LocalDate) {
        val oldRecord = recordsRepository.getRecord(id)
        val newRecord = oldRecord.copy(date = newDate)
        recordsRepository.updateRecord(newRecord)
        statsRepository.addRecord(oldRecord.copy(time = oldRecord.time.negated()))
        statsRepository.addRecord(newRecord)
    }
}
