package com.maxpoliakov.skillapp.util.stopwatch

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Duration.ZERO


class StopwatchUtilImplTest : StringSpec({
    suspend fun verifyCountsProperly(stopwatch: StopwatchUtilImpl) {
        stopwatch.state.value shouldBe Running(ZERO, skillId)
        stopwatch.toggle(skillId)
        delay(1000)
        stopwatch.state.value shouldBe Running(Duration.ofSeconds(1), skillId)
    }

    "ticks the time properly" {
        val stopwatch = StopwatchUtilImpl(CoroutineScope(Unconfined))
        stopwatch.state.value shouldBe Paused
        stopwatch.toggle(skillId)
        verifyCountsProperly(stopwatch)
        stopwatch.state.value shouldBe Paused
        verifyCountsProperly(stopwatch)
    }
}) {
    companion object {
        private const val skillId = 12
    }
}
