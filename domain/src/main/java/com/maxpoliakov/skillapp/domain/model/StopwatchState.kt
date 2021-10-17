package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.until
import java.time.ZonedDateTime

sealed class StopwatchState {
    data class Running(
        val startTime: ZonedDateTime,
        val skillId: Int
    ) : StopwatchState() {
        val time get() = startTime.until(getZonedDateTime())
    }

    object Paused : StopwatchState()
}
