package com.jdevs.timeo.util.time

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FormattingTest {

    @Test
    fun getFriendlyTime() {

        assertThat(getFriendlyTime(89), `is`("1h 29m"))
        assertThat(getFriendlyTime(60), `is`("1h"))
        assertThat(getFriendlyTime(0), `is`(""))
        assertThat(getFriendlyTime(1439), `is`("23h 59m"))
    }

    @Test
    fun getHours() {

        assertThat(getHours(90), `is`("1.5"))
        assertThat(getHours(0), `is`("0"))
        assertThat(getHours(102), `is`("1.7"))
        assertThat(getHours(101), `is`("1.7"))
    }
}
