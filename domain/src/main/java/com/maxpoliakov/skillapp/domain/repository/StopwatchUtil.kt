package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow

interface StopwatchUtil {
    val state: StateFlow<StopwatchState>

    fun start(skillId: Int)
    fun stop(): Deferred<Record?>
    fun cancel()
    fun toggle(skillId: Int): Deferred<Record?>
    fun updateNotification()
}
