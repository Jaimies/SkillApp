package com.maxpoliakov.skillapp.domain.usecase.records

interface ChangeRecordTimeUseCase {
    suspend fun run(id: Int, count: Long)
}
