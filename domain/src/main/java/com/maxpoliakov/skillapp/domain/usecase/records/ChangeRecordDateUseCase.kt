package com.maxpoliakov.skillapp.domain.usecase.records

import java.time.LocalDate

interface ChangeRecordDateUseCase {
    suspend fun run(id: Int, newDate: LocalDate)
}
