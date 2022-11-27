package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Id
import java.time.LocalDate

interface EditRecordUseCase {
    suspend fun changeDate(recordId: Id, newDate: LocalDate)
    suspend fun changeCount(recordId: Id, newCount: Long)
}
