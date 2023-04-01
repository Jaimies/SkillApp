package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StubStopwatch @Inject constructor() : Stopwatch {
    private val _state = MutableStateFlow<Stopwatch.State>(getRunningState())

    override val state = _state.asStateFlow()

    override suspend fun start(skillId: Int) = listOf<Record>()

    override suspend fun stop(): List<Record> {
        _state.value = Stopwatch.State.Paused
        return listOf()
    }

    override fun cancel() {}
    override suspend fun toggle(skillId: Int) = listOf<Record>()

    private fun getStartTime() = ZonedDateTime.now().minusHours(1).minusMinutes(2)
    private fun getRunningState() = Stopwatch.State.Running(getStartTime(), 3, -1)

    override fun updateNotification() {}
    override fun updateState() {}
}
