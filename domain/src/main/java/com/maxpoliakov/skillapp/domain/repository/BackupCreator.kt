package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.result.BackupCreationResult

interface BackupCreator {
    suspend fun create(): BackupCreationResult
}
