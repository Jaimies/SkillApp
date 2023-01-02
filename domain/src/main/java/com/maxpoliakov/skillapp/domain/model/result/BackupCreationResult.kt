package com.maxpoliakov.skillapp.domain.model.result

import com.maxpoliakov.skillapp.domain.model.BackupData

sealed class BackupCreationResult {
    class Success(val data: BackupData) : BackupCreationResult()
    class Failure(val exception: Throwable) : BackupCreationResult()
}
