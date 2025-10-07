package com.theskillapp.skillapp.data.backup.shared_storage

import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.theskillapp.skillapp.domain.repository.BackupRepository.Result
import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SharedStorageBackupConfigurationManager @Inject constructor(
    userPreferenceRepository: UserPreferenceRepository,
) : BackupConfigurationManager {

    override val configuration = userPreferenceRepository.backupExportDirectory.map { uri ->
        if (uri == null) Configuration.Failure(Result.Failure.NotConfigured)
        else Configuration.Success(uri)
    }

    override fun handleException(throwable: Throwable): Result.Failure {
        return Result.Failure.Error(throwable)
    }
}
