package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.BackupData

interface BackupCreator {
    suspend fun create(): BackupData
}
