package com.maxpoliakov.skillapp.domain.repository

interface BackupRestorer {
    suspend fun restore(backup: String)
}
