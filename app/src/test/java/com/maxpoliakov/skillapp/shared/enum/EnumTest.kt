package com.maxpoliakov.skillapp.shared.enum

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

enum class TestEnum {
    First, Second, Third
}

class EnumTest : StringSpec({
    "enumHasValue" {
        enumHasValue<TestEnum>("First") shouldBe true
        enumHasValue<TestEnum>("Second") shouldBe true
        enumHasValue<TestEnum>("Third") shouldBe true
        enumHasValue<TestEnum>("Forth") shouldBe false
        enumHasValue<TestEnum>("random") shouldBe false
    }
})
