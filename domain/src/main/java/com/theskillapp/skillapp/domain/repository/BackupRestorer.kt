package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.BackupData

interface BackupRestorer {
    suspend fun restore(data: BackupData): Result

    sealed class Result {
        object Success : Result()
        data class Failure(val exception: Throwable) : Result()
    }
}
