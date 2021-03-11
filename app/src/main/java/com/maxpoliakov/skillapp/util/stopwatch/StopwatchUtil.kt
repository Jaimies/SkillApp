package com.maxpoliakov.skillapp.util.stopwatch

import kotlinx.coroutines.flow.StateFlow

interface StopwatchUtil {
    val state: StateFlow<StopwatchState>

    fun start(skillId: Int)
    fun stop()
    fun toggle(skillId: Int)
    fun updateNotification()
}
