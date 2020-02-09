package com.jdevs.timeo.util.time

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ConvertersTest {

    @Test
    fun getMins() {

        assertThat(getMins(10, 30), `is`(630L))
        assertThat(getMins(3, 49), `is`(229L))
        assertThat(getMins(18, 17), `is`(1097L))
    }
}
