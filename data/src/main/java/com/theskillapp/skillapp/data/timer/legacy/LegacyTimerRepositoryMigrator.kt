package com.theskillapp.skillapp.data.timer.legacy

import com.theskillapp.skillapp.domain.repository.TimerRepository

interface LegacyTimerRepositoryMigrator {
    suspend fun migrate(target: TimerRepository)
}
