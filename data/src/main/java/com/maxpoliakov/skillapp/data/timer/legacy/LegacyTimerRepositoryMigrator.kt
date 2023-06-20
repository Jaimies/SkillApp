package com.maxpoliakov.skillapp.data.timer.legacy

import com.maxpoliakov.skillapp.domain.repository.TimerRepository

interface LegacyTimerRepositoryMigrator {
    suspend fun migrate(target: TimerRepository)
}
