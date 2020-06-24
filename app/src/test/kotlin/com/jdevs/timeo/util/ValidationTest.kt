package com.jdevs.timeo.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ValidationTest {

    @Test
    fun validatePassword_valid() {
        assertThat(validatePassword("complex_passWord1234"), `is`(VALID))
    }

    @Test
    fun validatePassword_empty() {
        assertThat(validatePassword(""), `is`(EMPTY))
    }

    @Test
    fun validatePassword_tooShort() {
        assertThat(validatePassword("short"), `is`(TOO_SHORT))
    }

    @Test
    fun validatePassword_tooLong() {
        val password = "tooLongComplexPasswordThatExceedsTheMaximalLength"
        assertThat(validatePassword(password), `is`(TOO_LONG))
    }

    @Test
    fun validateEmail_valid() {
        assertThat(validateEmail("john.doe@gmail.com"), `is`(VALID))
    }

    @Test
    fun validateEmail_empty() {
        assertThat(validateEmail(""), `is`(EMPTY))
    }

    @Test
    fun validateEmail_invalid() {
        assertThat(validateEmail("john.doe"), `is`(INVALID))
        assertThat(validateEmail("john@doe"), `is`(INVALID))
    }
}
