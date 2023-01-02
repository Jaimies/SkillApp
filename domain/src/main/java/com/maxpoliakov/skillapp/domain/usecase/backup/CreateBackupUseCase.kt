package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.result.BackupResult

interface CreateBackupUseCase {
    suspend fun createBackup(): BackupResult
}
