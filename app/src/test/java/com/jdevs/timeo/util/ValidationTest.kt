package com.jdevs.timeo.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * Unit tests for Validation utilities
 * Tests are for methods [String.validateEmail] and [String.validatePassword]
 */
class ValidationTest {

    @Test
    fun validatePassword_valid_returnsValid() {

        val password = "complex_passWord1234"

        assertThat(password.validatePassword(), `is`(VALID))
    }

    @Test
    fun validatePassword_empty_returnsEmpty() {

        val password = ""

        assertThat(password.validatePassword(), `is`(EMPTY))
    }

    @Test
    fun validatePassword_tooShort_returnsTooShort() {

        val password = "short"

        assertThat(password.validatePassword(), `is`(TOO_SHORT))
    }

    @Test
    fun validatePassword_tooLong_returnsTooLong() {

        val password = "tooLongComplexPasswordThatExceedsTheMaximalLength"

        assertThat(password.validatePassword(), `is`(TOO_LONG))
    }

    @Test
    fun validateEmail_valid_returnsValid() {

        val email = "john.doe@gmail.com"

        assertThat(email.validateEmail(), `is`(VALID))
    }

    @Test
    fun validateEmail_empty_returnsEmpty() {

        val email = ""

        assertThat(email.validateEmail(), `is`(EMPTY))
    }

    @Test
    fun validateEmail_invalid_returnsInvalid() {

        val emails = listOf("john.doe", "john@doe")

        emails.forEach {

            assertThat(it.validateEmail(), `is`(INVALID))
        }
    }
}
