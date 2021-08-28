package com.maxpoliakov.skillapp.domain.repository

interface BackupUtil {
    suspend fun getDatabaseBackup(): String

    suspend fun restoreBackup(backupData: String)
}
