package com.jdevs.timeo.usecases

import com.jdevs.timeo.data.source.RecordsRepository
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(
    private val recordsRepository: RecordsRepository
) {

    val records get() = recordsRepository.records

    fun resetRecords() = recordsRepository.resetRecordsMonitor()
}
