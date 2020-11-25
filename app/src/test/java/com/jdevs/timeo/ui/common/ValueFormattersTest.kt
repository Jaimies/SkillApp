package com.jdevs.timeo.ui.common

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValueFormattersTest : StringSpec({
    "TimeFormatter" {
        val formatter = TimeFormatter()
        formatter.getFormattedValue(0f) shouldBe ""
        formatter.getFormattedValue(1f) shouldBe "1m"
        formatter.getFormattedValue(2f) shouldBe "2m"
        formatter.getFormattedValue(60f) shouldBe "1h"
        formatter.getFormattedValue(64f) shouldBe "1.1h"
    }

    "WeekDayFormatter" {
        val formatter = WeekDayFormatter()
        // Epoch is a Thursday
        formatter.getFormattedValue(0f) shouldBe "Thu"
        formatter.getFormattedValue(1f) shouldBe "Fri"
        formatter.getFormattedValue(4f) shouldBe "Mon"
    }
})
