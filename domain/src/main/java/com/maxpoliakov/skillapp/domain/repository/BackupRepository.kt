package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult

interface BackupRepository {
    suspend fun upload(data: BackupData): BackupUploadResult

    suspend fun getBackups(): List<Backup>
    suspend fun getLastBackup(): Backup?
    suspend fun getContents(backup: Backup): BackupData
}
