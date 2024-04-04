package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StubRestoreBackupUseCase(
    state: RestoreBackupUseCase.RestorationState = RestoreBackupUseCase.RestorationState.Inactive,
    private val restorationResult: RestoreBackupUseCase.Result = RestoreBackupUseCase.Result.Success,
): RestoreBackupUseCase {
    override val state = MutableStateFlow(state).asStateFlow()
    override suspend fun restoreBackup(backup: Backup) = restorationResult
}