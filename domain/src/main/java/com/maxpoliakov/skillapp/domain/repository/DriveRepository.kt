package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Backup

interface DriveRepository {
    suspend fun uploadBackup(content: String)

    suspend fun getBackups(): List<Backup>
    suspend fun getLastBackup(): Backup?
    suspend fun getBackupContents(backup: Backup): String
}
