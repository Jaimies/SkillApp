package com.jdevs.timeo.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ListTest: StringSpec({
    "prepends an element" {
        val list = mutableListOf(2, 3)
        list.unshift(1) shouldBe listOf(1, 2, 3)
    }
})
