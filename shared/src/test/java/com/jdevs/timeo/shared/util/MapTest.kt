package com.jdevs.timeo.shared.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MapTest : StringSpec({
    "sumBy" {
        mapOf("a" to "b").sumBy { 1 } shouldBe 1
        mapOf("a" to "b", "c" to "d").sumBy { 2 } shouldBe 4
    }
})