package com.maxpoliakov.skillapp.shared.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class DurationTest : StringSpec({
    "sumByDuration" {
        listOf<Duration>().sumByDuration { it } shouldBe Duration.ZERO
        listOf(Duration.ofHours(1)).sumByDuration { it } shouldBe Duration.ofHours(1)
        listOf(
            Duration.ofHours(1),
            Duration.ofHours(2)
        ).sumByDuration { it } shouldBe Duration.ofHours(3)
    }

    "sum" {
        arrayOf<Duration>().sum() shouldBe Duration.ZERO
        arrayOf(Duration.ofHours(1)).sum() shouldBe Duration.ofHours(1)
        arrayOf(
            Duration.ofHours(1),
            Duration.ofHours(2)
        ).sum() shouldBe Duration.ofHours(3)
    }
})
