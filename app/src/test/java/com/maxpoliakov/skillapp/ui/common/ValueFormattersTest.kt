package com.maxpoliakov.skillapp.ui.common

import com.maxpoliakov.skillapp.createMockContext
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValueFormattersTest : StringSpec({
    "TimeFormatter" {
        val formatter = UiMeasurementUnit.Millis.getValueFormatter(createMockContext())
        formatter.getFormattedValue(0f) shouldBe ""
        formatter.getFormattedValue(1_000f) shouldBe "1m"
        formatter.getFormattedValue(60_000f) shouldBe "1m"
        formatter.getFormattedValue(120_000f) shouldBe "2m"
        formatter.getFormattedValue(3_600_000f) shouldBe "1h"
        formatter.getFormattedValue(3_840_000f) shouldBe "1.1h"
    }

    "WeekDayFormatter" {
        val formatter = DayFormatter()
        formatter.getFormattedValue(0f) shouldBe "1\nJan"
        formatter.getFormattedValue(1f) shouldBe "2\nJan"
        formatter.getFormattedValue(4f) shouldBe "5\nJan"
    }
})
