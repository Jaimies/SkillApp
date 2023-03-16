package com.maxpoliakov.skillapp.shared.chart.valueformatter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DayFormatterTest : StringSpec({
    "getFormattedValue()" {
        val formatter = DayFormatter()
        formatter.getFormattedValue(0f) shouldBe "1\nJan"
        formatter.getFormattedValue(1f) shouldBe "2\nJan"
        formatter.getFormattedValue(4f) shouldBe "5\nJan"
    }
})
