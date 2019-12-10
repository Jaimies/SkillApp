package com.jdevs.timeo.util

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class RandomStringTest {

    @Test
    fun randomString_thousandValues_noSame() {

        val randomStrings = (1..1000).map { randomString() }

        randomStrings.forEach { randomString ->

            val count = randomStrings.count { it == randomString }

            assertThat(count, `is`(1))
        }
    }
}
