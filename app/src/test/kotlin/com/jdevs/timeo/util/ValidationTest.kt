package com.jdevs.timeo.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class ValidationTest : StringSpec({

    "password validation" {
        forAll(
            row("complex_passWord1234", VALID),
            row("", EMPTY),
            row("short", TOO_SHORT),
            row("tooLongComplexPasswordThatExceedsTheMaximalLength", TOO_LONG)
        ) { password, expectedResult ->
            validatePassword(password) shouldBe expectedResult
        }
    }

    "email validation" {
        forAll(
            row("john.doe@gmail.com", VALID),
            row("", EMPTY),
            row("john.doe", INVALID),
            row("john@doe", INVALID)
        ) { email, expectedResult ->
            validateEmail(email) shouldBe expectedResult
        }
    }
})