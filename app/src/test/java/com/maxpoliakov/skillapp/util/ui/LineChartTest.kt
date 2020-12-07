package com.maxpoliakov.skillapp.util.ui

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class LineChartTest : StringSpec({
    "getAxisMaximum()" {
        getAxisMaximum(0f) shouldBe 0f
        getAxisMaximum(1f) shouldBe 60f
        getAxisMaximum(61f) shouldBe 120f
        getAxisMaximum(121f) shouldBe 240f
        getAxisMaximum(241f) shouldBe 360f
    }
})
