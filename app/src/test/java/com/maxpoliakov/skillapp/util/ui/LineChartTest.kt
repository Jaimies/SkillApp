package com.maxpoliakov.skillapp.util.ui

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LineChartTest : StringSpec({
    "getAxisMaximum()" {
        getAxisMaximum(0f) shouldBe 0f
        getAxisMaximum(1f) shouldBe 1800f
        getAxisMaximum(1801f) shouldBe 3600f
        getAxisMaximum(7201f) shouldBe 10800f
        getAxisMaximum(10801f) shouldBe 14400f
    }
})
