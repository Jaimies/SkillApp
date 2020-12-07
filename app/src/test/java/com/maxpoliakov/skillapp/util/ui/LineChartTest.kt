package com.maxpoliakov.skillapp.util.ui

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LineChartTest : StringSpec({
    "getAxisMaximum()" {
        getAxisMaximum(0f) shouldBe 0f
        getAxisMaximum(1f) shouldBe 30f
        getAxisMaximum(31f) shouldBe 60f
        getAxisMaximum(121f) shouldBe 180f
        getAxisMaximum(181f) shouldBe 240f
    }
})
