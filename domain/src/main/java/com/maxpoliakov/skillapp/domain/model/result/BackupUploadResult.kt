package com.maxpoliakov.skillapp.domain.model.result

import java.io.IOException

sealed class BackupUploadResult {
    object Success : BackupUploadResult()

    sealed class Failure : BackupUploadResult() {
        object NoInternetConnection : Failure()
        object Unauthorized : Failure()
        object PermissionDenied : Failure()
        object QuotaExceeded : Failure()

        class IOFailure(val exception: IOException) : Failure()
        class Error(val exception: Throwable) : Failure()
    }
}
