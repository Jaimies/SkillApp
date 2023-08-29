package com.maxpoliakov.skillapp.shared.extensions

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FormattingTest : StringSpec({
    "to readable float" {
        1f.toReadableFloat() shouldBe "1"
        0f.toReadableFloat() shouldBe "0"
        0.5f.toReadableFloat() shouldBe "0.5"
    }
})
