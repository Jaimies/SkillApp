package com.maxpoliakov.skillapp.domain.repository

interface BackupCreator {
    suspend fun create(): String
}
