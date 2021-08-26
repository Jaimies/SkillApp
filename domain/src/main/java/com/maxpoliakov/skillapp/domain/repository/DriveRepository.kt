package com.maxpoliakov.skillapp.domain.repository

interface DriveRepository {
    suspend fun uploadBackup(content: String)

    suspend fun getBackups(): List<String>
}
