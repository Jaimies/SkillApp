package com.maxpoliakov.skillapp.domain.stopwatch

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.LegacyTimerRepository

class StubLegacyTimerRepository(
    private val state: Timer?,
) : LegacyTimerRepository {
    override fun getTimer() = state
}
