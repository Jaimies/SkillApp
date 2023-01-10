package com.maxpoliakov.skillapp.domain.model.result

sealed class BackupRestorationResult {
    object Success : BackupRestorationResult()
    class Failure(val exception: Throwable) : BackupRestorationResult()
}
