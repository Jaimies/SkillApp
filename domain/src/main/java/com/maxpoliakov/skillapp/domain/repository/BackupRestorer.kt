package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.BackupData

interface BackupRestorer {
    suspend fun restore(data: BackupData)
}
