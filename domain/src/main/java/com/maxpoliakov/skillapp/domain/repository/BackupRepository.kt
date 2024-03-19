package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import kotlinx.coroutines.flow.Flow
import java.io.IOException

interface BackupRepository {
    suspend fun save(data: BackupData): Result<Unit>

    suspend fun getBackups(): Result<List<Backup>>
    suspend fun getLastBackup(): Result<Backup?>
    suspend fun getContents(backup: Backup): Result<BackupData>

    fun getLastBackupFlow(): Flow<Result<Backup?>>

    sealed class Result<out T> {
        data class Success<T>(val value: T) : Result<T>()

        sealed class Failure : Result<Nothing>() {
            object NoInternetConnection : Failure()
            object Unauthorized : Failure()
            object PermissionDenied : Failure()
            object QuotaExceeded : Failure()
            object NotConfigured: Failure()

            data class IOFailure(val exception: IOException) : Failure()
            data class Error(val exception: Throwable) : Failure()
        }
    }
}
