package com.theskillapp.skillapp.shared.chart.valueformatter

import com.theskillapp.skillapp.createMockContext
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TimeFormatterTest : StringSpec({
    "getFormattedValue()" {
        val formatter = TimeFormatter(createMockContext())
        formatter.getFormattedValue(0f) shouldBe ""
        formatter.getFormattedValue(1_000f) shouldBe "1m"
        formatter.getFormattedValue(60_000f) shouldBe "1m"
        formatter.getFormattedValue(120_000f) shouldBe "2m"
        formatter.getFormattedValue(3_600_000f) shouldBe "1h"
        formatter.getFormattedValue(3_840_000f) shouldBe "1.1h"
    }
})
