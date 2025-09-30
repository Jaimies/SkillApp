package com.theskillapp.skillapp.data.timer.legacy

import com.theskillapp.skillapp.domain.repository.LegacyTimerRepository
import com.theskillapp.skillapp.domain.repository.TimerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LegacyTimerRepositoryMigratorImpl @Inject constructor(
    private val legacyTimerRepository: LegacyTimerRepository,
): LegacyTimerRepositoryMigrator {
    override suspend fun migrate(target: TimerRepository) {
        val timer = legacyTimerRepository.getTimer() ?: return

        if (target.getAll().first().isEmpty()) {
            target.add(timer)
        }

        legacyTimerRepository.deleteTimer()
    }
}
