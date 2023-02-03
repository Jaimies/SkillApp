package com.maxpoliakov.skillapp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalTime

class RangeChangeTest : StringSpec({
    "Start" {
        RangeChange.Start<LocalTime>(newTime).apply(startTime..endTime) shouldBe newTime..endTime
    }

    "End" {
        RangeChange.End<LocalTime>(newTime).apply(startTime..endTime) shouldBe startTime..newTime
    }
}) {
    companion object {
        private val startTime = LocalTime.of(10, 0)
        private val endTime = LocalTime.of(12, 15)
        private val newTime = LocalTime.of(11, 0)
    }
}
