package com.maxpoliakov.skillapp.domain.usecase.backup

import kotlinx.coroutines.delay

class StubPerformBackupUseCase(
    private val result: PerformBackupUseCase.Result = PerformBackupUseCase.Result.Success,
    private val delay: Long = 0L,
) : PerformBackupUseCase {
    override suspend fun performBackup(): PerformBackupUseCase.Result {
        delay(delay)
        return result
    }
}
