package com.theskillapp.skillapp.domain.usecase.records

interface DeleteRecordUseCase {
    suspend fun run(id: Int)
}
