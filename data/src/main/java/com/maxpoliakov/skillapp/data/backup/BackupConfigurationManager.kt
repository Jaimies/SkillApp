package com.maxpoliakov.skillapp.data.backup

import com.maxpoliakov.skillapp.domain.model.GenericUri
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.flow.Flow

interface BackupConfigurationManager {
    val isConfigured: Flow<Boolean>
    val configurationFailureIfAny: Flow<BackupRepository.Result.Failure?>
    val directoryUri: Flow<GenericUri>

    fun handleException(throwable: Throwable): BackupRepository.Result.Failure
}
