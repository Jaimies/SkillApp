package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    override fun stop(): Deferred<Record?> {
        return CoroutineScope(Dispatchers.Default).async { null }
    }

    override fun cancel() {}
    override fun toggle(skillId: Int): Deferred<Record?> {
        return CoroutineScope(Dispatchers.Default).async { null }
    }

    override fun updateNotification() {}
}
