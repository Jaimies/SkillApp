package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.model.Record

interface AddRecordUseCase {
    suspend fun run(record: Record): Long
}
