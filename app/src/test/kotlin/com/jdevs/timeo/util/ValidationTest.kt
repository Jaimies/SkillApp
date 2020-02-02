package com.jdevs.timeo.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * Tests for validation utilities
 */
class ValidationTest {

    @Test
    fun validatePassword_valid_returnsValid() {

        assertThat(validatePassword("complex_passWord1234"), `is`(VALID))
    }

    @Test
    fun validatePassword_empty_returnsEmpty() {

        assertThat(validatePassword(""), `is`(EMPTY))
    }

    @Test
    fun validatePassword_tooShort_returnsTooShort() {

        assertThat(validatePassword("short"), `is`(TOO_SHORT))
    }

    @Test
    fun validatePassword_tooLong_returnsTooLong() {

        val password = "tooLongComplexPasswordThatExceedsTheMaximalLength"

        assertThat(validatePassword(password), `is`(TOO_LONG))
    }

    @Test
    fun validateEmail_valid_returnsValid() {

        assertThat(validateEmail("john.doe@gmail.com"), `is`(VALID))
    }

    @Test
    fun validateEmail_empty_returnsEmpty() {

        assertThat(validateEmail(""), `is`(EMPTY))
    }

    @Test
    fun validateEmail_invalid_returnsInvalid() {

        val emails = listOf("john.doe", "john@doe")

        emails.forEach {

            assertThat(validateEmail(it), `is`(INVALID))
        }
    }
}
