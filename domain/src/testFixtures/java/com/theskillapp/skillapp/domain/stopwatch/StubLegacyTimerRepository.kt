package com.theskillapp.skillapp.domain.stopwatch

import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.repository.LegacyTimerRepository

class StubLegacyTimerRepository(
    private val timer: Timer?,
) : LegacyTimerRepository {
    override fun getTimer() = timer
    override fun deleteTimer() {}
}
