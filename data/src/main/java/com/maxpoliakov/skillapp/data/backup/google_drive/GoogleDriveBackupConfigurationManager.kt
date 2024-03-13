package com.maxpoliakov.skillapp.data.backup.google_drive

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.domain.model.GenericUri
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import com.maxpoliakov.skillapp.domain.repository.NetworkUtil
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class GoogleDriveBackupConfigurationManager @Inject constructor(
    authRepository: AuthRepository,
    private val networkUtil: NetworkUtil,
) : BackupConfigurationManager {
    override val directoryUri = flowOf(GenericUri("appDataFolder"))

    override val configurationFailureIfAny = authRepository.currentUser.map { user ->
        when {
            user == null -> Result.Failure.Unauthorized
            !user.hasAppDataPermission -> Result.Failure.PermissionDenied
            !networkUtil.isConnected -> Result.Failure.NoInternetConnection
            else -> null
        }
    }

    override val isConfigured = configurationFailureIfAny.map { it == null }

    override fun handleException(throwable: Throwable): Result.Failure {
        return if (quotaExceeded(throwable)) {
            Result.Failure.QuotaExceeded
        } else {
            throwable.printStackTrace()
            if (throwable is IOException) Result.Failure.IOFailure(throwable)
            else Result.Failure.Error(throwable)
        }
    }

    private fun quotaExceeded(throwable: Throwable): Boolean {
        return throwable is GoogleJsonResponseException
                && throwable.details?.errors?.first()?.reason == "storageQuotaExceeded"
    }
}
