package com.jdevs.timeo.util.time

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ConvertersTest : StringSpec({

    "get friendly time" {
        getFriendlyTime(89) shouldBe "1h 29m"
        getFriendlyTime(60) shouldBe "1h"
        getFriendlyTime(0) shouldBe ""
        getFriendlyTime(1439) shouldBe "23h 59m"
    }

    "to readable float" {
        1f.toReadableFloat() shouldBe "1"
        0f.toReadableFloat() shouldBe "0"
        0.5f.toReadableFloat() shouldBe "0.5"
    }

    "get friendly hours" {
        getFriendlyHours(120) shouldBe "2"
        getFriendlyHours(0) shouldBe "0"
        getFriendlyHours(102) shouldBe "1.7"
        getFriendlyHours(101) shouldBe "1.7"
    }

    "get minutes" {
        getMins(10, 30) shouldBe 630
        getMins(3, 49) shouldBe 229
        getMins(18, 17) shouldBe 1097
    }
})
