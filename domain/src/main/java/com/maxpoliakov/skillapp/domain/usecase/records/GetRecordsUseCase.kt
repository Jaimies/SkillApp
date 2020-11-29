package com.maxpoliakov.skillapp.domain.usecase.records

import com.maxpoliakov.skillapp.domain.repository.RecordsRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(private val recordsRepository: RecordsRepository) {
    fun run() = recordsRepository.getRecords()
}