package com.jdevs.timeo.domain.records

import com.jdevs.timeo.data.records.RecordsRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(private val recordsRepository: RecordsRepository) {

    val records get() = recordsRepository.records

    fun resetRecords() = recordsRepository.resetMonitor()
}
