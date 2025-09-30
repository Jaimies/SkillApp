package com.theskillapp.skillapp.shared.chart.valueformatter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class IntegerFormatterTest: StringSpec({
    "getFormattedValue()" {
        val formatter = IntegerFormatter()
        formatter.getFormattedValue(0f) shouldBe ""
        formatter.getFormattedValue(1_000f) shouldBe "1000"
        formatter.getFormattedValue(60_000f) shouldBe "60000"
        formatter.getFormattedValue(120_000f) shouldBe "120000"
        formatter.getFormattedValue(3_600_000f) shouldBe "3600000"
        formatter.getFormattedValue(3_840_000f) shouldBe "3840000"
    }
})
