package com.maxpoliakov.skillapp.domain.repository

interface BackupUtil {
    suspend fun getDatabaseBackup(): String
}
