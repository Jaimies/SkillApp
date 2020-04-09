package com.jdevs.timeo.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ValidationTest : Spek({

    group("password validation") {

        describe("valid password") {
            assertThat(validatePassword("complex_passWord1234"), `is`(VALID))
        }

        describe("empty password") {
            assertThat(validatePassword(""), `is`(EMPTY))
        }

        describe("too short password") {
            assertThat(validatePassword("short"), `is`(TOO_SHORT))
        }

        describe("too long password") {
            val password = "tooLongPasswordThatExceedsTheMaximalLength"
            assertThat(validatePassword(password), `is`(TOO_LONG))
        }
    }

    group("email validation") {

        describe("valid email") {
            assertThat(validateEmail("john.doe@gmail.com"), `is`(VALID))
        }

        describe("empty email") {
            assertThat(validateEmail(""), `is`(EMPTY))
        }

        describe("invalid emails") {

            listOf("john.doe", "john@doe", "john").forEach {
                it(""""$it" should be an invalid email""") {
                    assertThat(validateEmail(it), `is`(INVALID))
                }
            }
        }
    }
})
