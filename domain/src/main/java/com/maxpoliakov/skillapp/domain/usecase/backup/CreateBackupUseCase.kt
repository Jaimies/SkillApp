package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult

interface CreateBackupUseCase {
    suspend fun createBackup(): BackupUploadResult
}
