package com.maxpoliakov.skillapp.domain.usecase.records

interface DeleteRecordUseCase {
    suspend fun run(id: Int)
}
