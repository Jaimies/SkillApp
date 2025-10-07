package com.theskillapp.skillapp.domain.usecase.records

import com.theskillapp.skillapp.domain.model.Record

interface AddRecordUseCase {
    suspend fun run(record: Record): Long
}
