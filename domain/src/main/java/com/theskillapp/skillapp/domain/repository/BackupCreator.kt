package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.BackupData

interface BackupCreator {
    suspend fun create(): Result

    sealed class Result {
        data class Success(val data: BackupData) : Result()
        data class Failure(val exception: Exception) : Result()
    }
}
