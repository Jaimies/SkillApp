package com.jdevs.timeo.domain.usecase.records

import com.jdevs.timeo.domain.repository.RecordsRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(private val recordsRepository: RecordsRepository) {
    fun run() = recordsRepository.records
}
