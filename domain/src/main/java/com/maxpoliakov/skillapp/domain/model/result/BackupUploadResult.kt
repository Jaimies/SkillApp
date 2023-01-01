package com.maxpoliakov.skillapp.domain.model.result

import java.io.IOException

sealed class BackupUploadResult {
    object Success : BackupUploadResult()
    object NoInternetConnection : BackupUploadResult()
    object Unauthorized : BackupUploadResult()
    object PermissionDenied : BackupUploadResult()
    object QuotaExceeded : BackupUploadResult()

    class IOFailure(val exception: IOException) : BackupUploadResult()
    class Error(val exception: Throwable) : BackupUploadResult()
}
