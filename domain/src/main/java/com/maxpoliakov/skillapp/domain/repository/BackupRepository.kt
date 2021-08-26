package com.maxpoliakov.skillapp.domain.repository

interface BackupRepository {
    suspend fun createBackup(content: String)

    suspend fun getBackups (): List<String>
}
