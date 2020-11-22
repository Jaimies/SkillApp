package com.jdevs.timeo.util.time

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class ConvertersTest : StringSpec({
    "get friendly time" {
        getFriendlyTime(Duration.ofMinutes(89)) shouldBe "1h 29m"
        getFriendlyTime(Duration.ofMinutes(60)) shouldBe "1h"
        getFriendlyTime(Duration.ZERO) shouldBe ""
        getFriendlyTime(Duration.ofMinutes(1439)) shouldBe "23h 59m"
    }

    "to readable float" {
        1f.toReadableFloat() shouldBe "1"
        0f.toReadableFloat() shouldBe "0"
        0.5f.toReadableFloat() shouldBe "0.5"
    }

    "get friendly hours" {
        getFriendlyHours(Duration.ofMinutes(120)) shouldBe "2"
        getFriendlyHours(Duration.ZERO) shouldBe "0"
        getFriendlyHours(Duration.ofMinutes(102)) shouldBe "1.7"
        getFriendlyHours(Duration.ofMinutes(101)) shouldBe "1.7"
    }
})
