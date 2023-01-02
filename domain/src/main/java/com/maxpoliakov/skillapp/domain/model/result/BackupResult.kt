package com.maxpoliakov.skillapp.domain.model.result

sealed class BackupResult {
    object Success : BackupResult()
    class CreationFailure(val creationResult: BackupCreationResult.Failure) : BackupResult()
    class UploadFailure(val uploadResult: BackupUploadResult.Failure) : BackupResult()
}
