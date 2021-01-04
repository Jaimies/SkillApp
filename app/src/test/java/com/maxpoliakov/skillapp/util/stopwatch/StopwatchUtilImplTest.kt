package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.delay
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.Duration
import java.time.Duration.ZERO


class StopwatchUtilImplTest : StringSpec({
    val callback = mock({ _: Duration -> }::class.java)

    "ticks the time properly" {
        val stopwatch = StopwatchUtilImpl(CoroutineScope(Unconfined))
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId, callback)
        stopwatch.state.value shouldBe Running(ZERO, skillId)
        delay(1000)
        stopwatch.state.value shouldBe Running(Duration.ofSeconds(1), skillId)
        stopwatch.toggle(skillId, callback)
        verify(callback).invoke(Duration.ofSeconds(1))
    }
}) {
    companion object {
        private const val skillId = 12
    }
}