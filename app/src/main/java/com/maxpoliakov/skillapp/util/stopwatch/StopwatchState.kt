package com.maxpoliakov.skillapp.util.stopwatch

import java.time.Duration

sealed class StopwatchState {
    data class Running(
        val time: Duration,
        val skillId: Int
    ) : StopwatchState()

    object Paused : StopwatchState()
}
