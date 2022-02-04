package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.StopwatchState
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StubStopwatchUtil @Inject constructor() : StopwatchUtil {
    private val _state = MutableStateFlow<StopwatchState>(getRunningState())

    override val state = _state.asStateFlow()

    override suspend fun start(skillId: Int): Record? = null

    override suspend fun stop(): Record? {
        _state.value = StopwatchState.Paused
        return null
    }

    override fun cancel() {}
    override suspend fun toggle(skillId: Int): Record? = null

    private fun getStartTime() = ZonedDateTime.now().minusHours(1).minusMinutes(2)
    private fun getRunningState() = StopwatchState.Running(getStartTime(), 3, -1)

    override fun updateNotification() {}
    override fun updateState() {}
}
