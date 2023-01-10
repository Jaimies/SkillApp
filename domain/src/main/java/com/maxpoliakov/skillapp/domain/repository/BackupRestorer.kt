package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.result.BackupRestorationResult

interface BackupRestorer {
    suspend fun restore(data: BackupData): BackupRestorationResult
}
