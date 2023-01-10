package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import kotlinx.coroutines.flow.StateFlow

interface RestoreBackupUseCase {
    val state: StateFlow<RestorationState>

    suspend fun restoreBackup(backup: Backup)

    enum class RestorationState {
        Active, Inactive,
    }
}
