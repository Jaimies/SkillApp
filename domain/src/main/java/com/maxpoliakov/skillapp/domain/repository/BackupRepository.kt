package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Backup

interface BackupRepository {
    suspend fun upload(content: String)

    suspend fun getBackups(): List<Backup>
    suspend fun getLastBackup(): Backup?
    suspend fun getContents(backup: Backup): String
}
