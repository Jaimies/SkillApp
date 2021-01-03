package com.maxpoliakov.skillapp.util.stopwatch

import kotlinx.coroutines.flow.StateFlow

interface StopwatchUtil {
    val state: StateFlow<StopwatchState>
    fun toggle(skillId: Int)
}
