package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.StopwatchState
import kotlinx.coroutines.flow.StateFlow

interface StopwatchUtil {
    val state: StateFlow<StopwatchState>

    fun start(skillId: Int)
    fun stop()
    fun toggle(skillId: Int)
    fun updateNotification()
}
