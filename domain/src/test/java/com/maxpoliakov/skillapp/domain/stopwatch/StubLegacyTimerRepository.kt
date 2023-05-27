package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.LegacyTimerRepository

class StubLegacyTimerRepository(
    private val timer: Timer?,
) : LegacyTimerRepository {
    override fun getTimer() = timer
    override fun deleteTimer() {}
}
