package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime
import javax.inject.Inject

class StubStopwatchUtil @Inject constructor() : StopwatchUtil {
    override val state: StateFlow<StopwatchState>
        get() {
            val startTime = ZonedDateTime.now().minusHours(1).minusMinutes(2)
            return MutableStateFlow(StopwatchState.Running(startTime, 3))
        }

    override suspend fun start(skillId: Int): Record? = null
    override suspend fun stop(): Record? = null

    override fun cancel() {}
    override suspend fun toggle(skillId: Int): Record? = null

    override fun updateNotification() {}
}
