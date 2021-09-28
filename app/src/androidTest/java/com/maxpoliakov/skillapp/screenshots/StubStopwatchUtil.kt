package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
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

    override fun start(skillId: Int) {}
    override fun stop() {}
    override fun toggle(skillId: Int) {}

    override fun updateNotification() {}
}
