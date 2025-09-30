package com.theskillapp.skillapp.data.backup

import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.flow.Flow

interface BackupConfigurationManager {
    val configuration: Flow<Configuration>

    fun handleException(throwable: Throwable): BackupRepository.Result.Failure

    sealed class Configuration {
        data class Success(val directoryUri: GenericUri): Configuration()
        data class Failure(val failure: BackupRepository.Result.Failure): Configuration()
    }
}
