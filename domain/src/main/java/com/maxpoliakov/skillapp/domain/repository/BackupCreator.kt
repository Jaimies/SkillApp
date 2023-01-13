package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.BackupData

interface BackupCreator {
    suspend fun create(): Result

    sealed class Result {
        data class Success(val data: BackupData) : Result()
        data class Failure(val exception: Throwable) : Result()
    }
}
