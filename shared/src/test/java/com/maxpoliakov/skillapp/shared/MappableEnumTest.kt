package com.maxpoliakov.skillapp.shared

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

private enum class DomainTestEnum {
    A,
    B,
    C,
}

private enum class TestEnum : MappableEnum<TestEnum, DomainTestEnum> {
    A {
        override fun toDomain() = DomainTestEnum.A
    },
    B {
        override fun toDomain() = DomainTestEnum.B
    };

    companion object : MappableEnum.Companion<TestEnum, DomainTestEnum>(values())
}

class MappableEnumTest : StringSpec({
    "from(DomainType)" {
        TestEnum.from(DomainTestEnum.A) shouldBe TestEnum.A
        TestEnum.from(DomainTestEnum.B) shouldBe TestEnum.B
    }

    "from(DomainType) throws an error if no element in `values` matches the given DomainType" {
        val exception = shouldThrow<IllegalArgumentException> {
            TestEnum.from(DomainTestEnum.C)
        }
        exception.message shouldBe "Unknown enum value: C"
    }
})
