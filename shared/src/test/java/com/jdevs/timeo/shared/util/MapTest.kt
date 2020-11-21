package com.jdevs.timeo.shared.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class MapTest : StringSpec({
    "sumByDuration" {
        mapOf(0 to Duration.ZERO).sumByDuration() shouldBe Duration.ZERO

        mapOf(
            0 to Duration.ofDays(1),
            1 to Duration.ofDays(2)
        ).sumByDuration() shouldBe Duration.ofDays(3)
    }
})
